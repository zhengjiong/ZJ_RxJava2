package com.example.simple;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Title: Example6
 * Description:
 *
 * RxJava 2.0 最核心的是Publisher和Subscriber。Publisher可以发出一系列的事件，而Subscriber负责和处理这些事件。
 * 平常用得最多的Publisher是Flowable，它支持背压
 *
 * Copyright:Copyright(c)2016

 * CreateTime:17/3/29  16:03
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example3 {

    public static void main(String[] args){
        //test1();
        test2();

    }

    private static void test1() {
        Flowable.just("hello RxJava2")
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        /**
                         * 需要注意的是，在onSubscribe中，我们需要调用request去请求资源，参数就是要请求的数量，
                         * 一般如果不限制请求数量，可以写成Long.MAX_VALUE。如果你不调用request，
                         * Subscriber的onNext和onComplete方法将不会被调用。
                         */
                        s.request(1000);
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext " + s);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    private static void test2() {
        Flowable.just("hello RxJava2")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        System.out.println("accept " + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        System.out.println("onError");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("onComplete");
                    }
                }, new Consumer<Subscription>() {
                    @Override
                    public void accept(@NonNull Subscription subscription) throws Exception {
                        /**
                         * 这里如果要加上onSubscribe的Consumer, 就必须执行subscription.request方法,不然不会执行onNext,
                         * 也可以不加上onSubscrib的Consumer, 这样就不用写request方法.
                         */
                    }
                });
    }
}
