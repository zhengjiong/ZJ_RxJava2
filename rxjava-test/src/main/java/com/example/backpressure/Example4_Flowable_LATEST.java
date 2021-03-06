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
 * Created by zj on 2017/4/4.
 */
public class Example4_Flowable_LATEST {
    private static Subscription subscription;

    public static void main(String[] args) {
        //test1();
        test2();
    }

    /**
     * 这次是i<10000, 因为1秒后, for循环已经执行完了, 如果是用Drop那第二次request(128)没有任何输出,
     * 但是用Lastest后, 第二次会输出最后一个9999
     * 输出:
     * onSubscribe
     * onNext 1 Thread[RxNewThreadScheduler-2,5,main]
     * onNext 2 Thread[RxNewThreadScheduler-2,5,main]
     * ......
     * onNext 127 Thread[RxNewThreadScheduler-2,5,main]
     * onNext 128 Thread[RxNewThreadScheduler-2,5,main]
     * onNext 9999 Thread[RxNewThreadScheduler-2,5,main]
     */
    private static void test1() {
        /**
         * 这里必须使用Schedulers.newThread(),让事件源和订阅者运行在不同的线程上才不会造成线程堵塞而影响测试结果,
         * 最后还需要加上System.in.read();才会有打印输出
         *
         * 输出:
         */
        Flowable.create(new FlowableOnSubscribe<Long>() {
            @Override
            public void subscribe(final FlowableEmitter<Long> e) throws Exception {
                for (long i = 1; i < 10000; i++) {
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<Long>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                subscription = s;
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("onNext " + aLong + " " + Thread.currentThread().toString());
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
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * Flowable内部的默认的水缸大小为128, 因此使用drop后,它刚开始肯定会把1-128这128个事件保存起来,
         * 然后丢弃掉其余的事件, 当我们request(128)的时候,下游便会处理掉这128个事件, 那么上游水缸中又会重新装进新的128个事件,
         * 上游一直在发送事件, 第二次request的时候, 上游已经发送完了,并没有装填到水缸中,所有第二次request, 没有输出.
         */
        subscription.request(128);
        //因为默认水缸中最多只能存放128个(buffer除外),用129也只能最多请求128个
        //subscription.request(129);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        subscription.request(128);

        try {
            System.in.read();//必须要加上这一句才会有输出
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 因为一开始的订阅的时候就request了, 所以直接输出1-128, 然后这个时候上游还在不停的发出信息, 所以水缸会立马填充,
     * 但之后并不是从128之后开始填充的,因为上游和下游处理是异步的,上游还在不停的发送,下游还没有开始处理的时候也一直在发送;
     * 然后再次request的时候会输出后面的, 最后一次request因为上游只发送10000个,如果使用drop后面的事件直接被抛弃了,不会有任何输出!
     * 如果使用lastest会输出最后一个onNext 9999 Thread[RxNewThreadScheduler-1,5,main]
     *
     * 输出:
     * onSubscribe
     * onNext 1 Thread[RxNewThreadScheduler-2,5,main]
     * ......
     * onNext 128 Thread[RxNewThreadScheduler-2,5,main]
     * ......
     * onNext 1425 Thread[RxNewThreadScheduler-1,5,main]
     * onNext 1426 Thread[RxNewThreadScheduler-1,5,main]
     *
     * onNext 9999 Thread[RxNewThreadScheduler-1,5,main]
     */
    private static void test2() {
        /**
         * 这里必须使用Schedulers.newThread(),让事件源和订阅者运行在不同的线程上才不会造成线程堵塞而影响测试结果,
         * 最后还需要加上System.in.read();才会有打印输出
         *
         * 输出:
         */
        Flowable.create(new FlowableOnSubscribe<Long>() {
            @Override
            public void subscribe(final FlowableEmitter<Long> e) {
                for (long i = 1; i < 10000; i++) {
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<Long>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                subscription = s;
                //一开始就处理掉128个事件
                s.request(128);
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("onNext " + aLong + " " + Thread.currentThread().toString());
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
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * Flowable内部的默认的水缸大小为128, 因此使用drop后,它刚开始肯定会把1-128这128个事件保存起来,
         * 然后丢弃掉其余的事件, 当我们request(128)的时候,下游便会处理掉这128个事件, 那么上游水缸中又会重新装进新的128个事件,
         * 因为上游一直在发送事件,所以第二次request的时候, 不会从128开始, 而是很大的一个数字
         */
        subscription.request(128);
        //因为默认水缸中最多只能存放128个(buffer除外),用129也只能最多请求128个
        //subscription.request(129);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        subscription.request(128);

        try {
            System.in.read();//必须要加上这一句才会有输出
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
