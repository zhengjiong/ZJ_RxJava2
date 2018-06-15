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
 * BackpressureStrategy.BUFFER使用
 *
 * CreateTime:18/6/14  14:00
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example2_Flowable_BUFFER {

    //BackpressureStrategy.BUFFER
    public static void main(String[] args) {
        test1();
    }

    /**
     * 这次我们直接让上游发送了1000个事件,下游仍然不调用request去请求, 与之前不同的是, 这次我们用的策
     * 略是BackpressureStrategy.BUFFER, 这就是我们的新水缸啦, 这个水缸就比原来的水缸牛逼多了,如果
     * 说原来的水缸是95式步枪, 那这个新的水缸就好比黄金AK , 它没有大小限制, 因此可以存放许许多多的事件.
     *
     * 换了水缸的FLowable和Observable好像是一样的,不错, 这时的FLowable表现出来的特性的确
     * 和Observable一模一样, 因此, 如果你像这样单纯的使用Flowable, 同样需要注意OOM的问题.
     */
    private static void test1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 1000; i++) {
                    System.out.println("emmit " + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe");
                        //不调用request去请求
                        //s.request(1);
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
