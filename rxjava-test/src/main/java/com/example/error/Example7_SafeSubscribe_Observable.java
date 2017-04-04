package com.example.error;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.SafeObserver;
import io.reactivex.subscribers.SafeSubscriber;

/**
 * Created by zj on 2017/4/4.
 */

public class Example7_SafeSubscribe_Observable {

    public static void main(String[] args) {
        //test1();
        test2();
    }

    private static void test1() {
        Observable.just(1, 2)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer t) {
                        if (t == 1) {
                            /**
                             * 这里抛出异常会报错, Flowable不循序在onNext中抛出异常
                             * 就算是System.out.println(1/0)这样也不行,
                             * 如果要这样操作的话可以使用SafeObserver
                             * 看test2方法
                             */
                            //System.out.println(1/0);
                            throw new RuntimeException("error-1");
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    private static void test2() {

        Observable.just(1, 2)
                //使用SafeSubscriber才可以在onNext中抛出异常
                .subscribe(new SafeObserver<Integer>(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer t) {
                        if (t == 1) {
                            /**
                             * 这里抛出异常会报错, Observable不循序在onNext中抛出异常
                             * 就算是System.out.println(1/0)这样也不行,
                             * 如果要这样操作的话可以使用SafeObserver
                             * 看test2方法
                             */
                            //System.out.println(1/0);
                            throw new RuntimeException("error-1");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                }));
    }
}
