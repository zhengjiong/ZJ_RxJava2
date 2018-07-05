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
 * CreateTime:18/7/5  10:05
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example7_Flowable_requested {

    public static void main(String[] args) {
        //test1();
        test2();
    }

    /**
     * 这次是异步的情况，上游啥也不做，下游也啥也不做,运行结果:
     * onSubscribe
     * request amount 128
     */
    private static void test1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) {
                System.out.println("request amount " + e.requested());
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 可以看到，当上下游工作在不同的线程里时，每一个线程里都有一个requested，而我们调用request（1000）时，
     * 实际上改变的是下游主线程中的requested，而上游中的requested的值是由RxJava内部调用request(n)去设置的，
     * 这个调用会在合适的时候自动触发。
     * 现在我们就能理解为什么没有调用request，上游中的值是128了，因为下游在一开始就在内部调用了request(128)
     * 去设置了上游中的值，因此即使下游没有调用request()，上游也能发送128个事件，这也可以解释之前我们为什么
     * 说Flowable中默认的水缸大小是128，其实就是这里设置的。
     *
     *
     * 这次我们在下游调用了request（1000），按照之前我们说的，这次的运行结果应该是1000，来看看运行结果：
     * onSubscribe
     * request amount 128
     */
    private static void test2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) {
                System.out.println("request amount " + e.requested());
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                s.request(1000);
            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
