package com.example.error;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;

/**
 * Created by zj on 2017/3/30.
 */

public class Example1_Flowable {

    public static void main(String[] args) {
        //test1();
        test2();
    }


    private static void test1() {
        Flowable.<Integer>create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                e.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        System.out.println("cancel");//代表已经取消订阅
                    }
                });
                e.onNext(1);
                e.onNext(2);

                e.onComplete();//会导致取消订阅

                /**
                 * 注意: 和RxJava1的区别:
                 * RxJava2解除订阅后再次onError会导致程序崩溃!
                 * 这里可以加上if判断是否已经取消订阅,不会就会导致崩溃
                 */
                if (!e.isCancelled()) {
                    e.onError(new RuntimeException("error-2"));
                }
            }
        }, BackpressureStrategy.LATEST).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError " + t.toString());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    private static void test2() {
        Flowable.<Integer>create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                e.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        System.out.println("cancel");//代表已经取消订阅
                    }
                });
                e.onNext(1);
                e.onNext(2);

                e.onError(new RuntimeException("error-1"));//会导致取消订阅

                /**
                 * 注意: 和RxJava1的区别:
                 * RxJava2解除订阅后再次onError会导致程序崩溃!
                 * 这里可以加上if判断是否已经取消订阅,不会就会导致崩溃
                 */
                if (!e.isCancelled()) {
                    e.onError(new RuntimeException("error-2"));
                }
            }
        }, BackpressureStrategy.LATEST).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError " + t.toString());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
