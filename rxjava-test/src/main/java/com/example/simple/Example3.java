package com.example.simple;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Title: Example3
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
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
                         * 必须调用request, Flowable才会执行,Subscriber才能再onNext接收到值
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
