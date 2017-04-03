package com.example.simple;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Title: Example4
 * Description:
 *
 * RxJava 2.0 最核心的是Publisher和Subscriber。Publisher可以发出一系列的事件，而Subscriber负责和处理这些事件。
 * 平常用得最多的Publisher是Flowable，它支持背压
 *
 * Copyright:Copyright(c)2016

 * CreateTime:17/3/27  17:08
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example2 {
    public static void main(String[] args){
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                /**
                 * 需要注意的是，在onSubscribe中，我们需要调用request去请求资源，参数就是要请求的数量，
                 * 一般如果不限制请求数量，可以写成Long.MAX_VALUE。如果你不调用request，
                 * Subscriber的onNext和onComplete方法将不会被调用。
                 */
                System.out.println("onSubscribe 1");
                s.request(20);
                System.out.println("onSubscribe 2");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }

        };

        Flowable flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onComplete();
                e.onNext(3);
            }
        }, BackpressureStrategy.LATEST);

        flowable.doOnSubscribe(new Consumer<Subscription>() {
            @Override
            public void accept(@NonNull Subscription subscription) throws Exception {

            }
        }).subscribe(subscriber);
    }
}
