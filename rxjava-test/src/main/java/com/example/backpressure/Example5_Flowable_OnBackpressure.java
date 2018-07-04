package com.example.backpressure;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * CreateTime:18/7/3  08:56
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example5_Flowable_OnBackpressure {

    public static void main(String[] args) {
        test1();
    }

    /**
     * 输出:
     * onNext 0
     * onError io.reactivex.exceptions.MissingBackpressureException: Can't deliver value 128 due to lack of requests
     */
    private static void test1() {
        Flowable.interval(1, TimeUnit.MILLISECONDS).observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("onNext " + aLong);
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
