package com.example.backpressure;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * BackpressureStrategy.ERROR使用
 *
 * 当上下游工作在同一个线程中时, 这时候是一个同步的订阅关系, 也就是说上游每发送一个事件必须等到下游接收处理完了以后才能接着发送下一个事件.
 *
 * 当上下游工作在不同的线程中时, 这时候是一个异步的订阅关系, 这个时候上游发送数据不需要等待下游接收, 为什么呢,
 * 因为两个线程并不能直接进行通信, 因此上游发送的事件并不能直接到下游里去, 这个时候就需要一个田螺姑娘来帮助它们俩,
 * 这个田螺姑娘就是我们刚才说的水缸 ! 上游把事件发送到水缸里去, 下游从水缸里取出事件来处理, 因此, 当上游发事件的速度太快,
 * 下游取事件的速度太慢, 水缸就会迅速装满, 然后溢出来, 最后就OOM了.
 *
 * 作者：Season_zlc
 * 链接：https://www.jianshu.com/p/0f2d6c2387c9
 * 來源：简书
 * 简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
 *
 * CreateTime:18/6/14  13:17
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example1_Flowable_Error {

    public static void main(String[] args) {
        //test1();
        //test2();
        test3();
    }

    /**
     * 因为上游和下游工作在主线程中, 下游没有调用request或者只调用了request(1),数量小于上游发送的数量,
     * 这样上游就认为下游没有处理事件的能力, 而这又是一个同步的订阅, 既然下游处理不了, 那上游不可能一直等待吧,
     * 如果是这样, 万一这两根水管工作在主线程里, 界面不就卡死了吗, 因此只能抛个异常来提醒我们:io.reactivex.exceptions.MissingBackpressureException.
     * <p>
     * 输出:
     * onSubscribe
     * emit 1
     * onNext: 1
     * emit 2
     * io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests
     * emit 3
     * emit complete
     */
    private static void test1() {
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                System.out.println("emit 1");
                emitter.onNext(1);
                System.out.println("emit 2");
                emitter.onNext(2);
                System.out.println("emit 3");
                emitter.onNext(3);
                System.out.println("emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR); //增加了一个参数

        Subscriber<Integer> downstream = new Subscriber<Integer>() {

            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                /**
                 * 这里设置多少数量,下游就会收到多少数量.
                 * 所以我们把request当做是一种能力, 当成下游处理事件的能力, 下游能处理几个就告诉上游我要几个,
                 * 这样只要上游根据下游的处理能力来决定发送多少事件, 就不会造成一窝蜂的发出一堆事件来, 从而导致OOM.
                 */
                s.request(1);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext: " + integer);

            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        upstream.subscribe(downstream);
    }


    static Subscription subscription;

    /**
     * Flowable里默认有一个大小为128的水缸, 当上下游工作在不同的线程中时, 上游就会先把事件发送到这个水缸中,
     * 因此, 下游虽然没有调用request, 但是上游在水缸中保存着这些事件, 只有当下游调用request时, 才从水缸里取出事件发给下游.
     * <p>
     * 输出:
     * onSubscribe
     * emit 1
     * emit 2
     * emit 3
     * emit complete
     * onNext = 1
     * onNext = 2
     */
    private static void test2() {

        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                System.out.println("emit 1");
                emitter.onNext(1);
                System.out.println("emit 2");
                emitter.onNext(2);
                System.out.println("emit 3");
                emitter.onNext(3);
                System.out.println("emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe");
                        subscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext = " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError " + t);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });

        /**
         * 这里每执行一次request(1), onNext就会接收到上游发送的一次信息
         */
        subscription.request(1);
        subscription.request(1);

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 水缸的大小默认为128,这里我们让上游一次性发送了129个事件, 下游一个也不接收,
     * MissingBackpressureException异常, 提醒我们发洪水啦. 当然了, 这个128也不是我凭空捏造出来的, Flowable的源码中就有这个buffersize的大小定义, 可以自行查看.
     * 注意这里我们是把上游发送的事件全部都存进了水缸里, 下游一个也没有消费, 所以就溢出了, 如果下游去消费了事件, 可能就不会导致水缸溢出来了.
     * <p>
     * 输出:
     * emmit 0
     * emmit 1
     * ....
     * emmit 128
     * onError io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests
     */
    private static void test3() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 129; i++) {
                    System.out.println("emmit " + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext = " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError " + t);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
