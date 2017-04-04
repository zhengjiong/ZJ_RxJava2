package com.example.lifecycle;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Cancellable;

/**
 * Created by zj on 2017/3/30.
 */

public class Example0_Flowable {

    public static void main(String[] args) {
        //test1();
        test2();
    }

    /**
     * 输出:
     * onSubscribe
     * onNext 1
     * onNext 2
     * onComplete
     * dispose
     * send 3
     */
    private static void test1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                /**
                 * 用e.setDisposable();也可以, 但两者只能用一种,不能同时使用不然会出问题
                 */
                //e.setDisposable();
                e.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        //onComplete执行后, 会进入此方法, 代表已经解除订阅
                        System.out.println("cancel");
                    }
                });
                e.onNext(1);
                e.onNext(2);
                e.onComplete();//执行后会解除订阅

                /**
                 * 会执行下面这行代码, 但是不会在订阅者中执行onNext3,因为已经onCompleted了, 就已经取消订阅了
                 */
                System.out.println("send3");
                e.onNext(3);
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
                System.out.println("onError " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }


    private static void test2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                /**
                 * 用e.setDisposable();也可以, 但两者只能用一种,不能同时使用不然会出问题
                 */
                //e.setDisposable();
                e.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        //onComplete执行后, 会进入此方法, 代表已经解除订阅
                        System.out.println("cancel");
                    }
                });
                e.onNext(1);
                e.onNext(2);

                if (true) {
                    //执行后会解除订阅
                    throw new RuntimeException("error-1");
                }

                /**
                 * throw exception后, 不会执行以下代码
                 */
                System.out.println("send3");
                e.onNext(3);
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
                System.out.println("onError " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
