package com.example.lifecycle;

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
import io.reactivex.functions.Action;
import io.reactivex.functions.Cancellable;

/**
 * Title: Example4
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/3/30  13:44
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example5_Flowable {

    public static void main(String[] args){
        test1();
    }

    /**
     * 输出:
     * onSubscribe
     * onNext 1
     * doOnCancel
     * cancel
     */
    private static void test1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                /**
                 * 会直接打印出来,不管有没有取消都会打印!!!
                 * 如果使用Flowable,就不要使用setDisposable,应该使用setCancellable!
                 * 如果要使用setDisposable, 就不要使用setCancellable, 两者
                 * 只能选其一,不然直接默认回调第一个
                 */
                e.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        System.out.println("cancel");
                    }
                });
               /*e.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        System.out.println("dispose");
                    }

                    @Override
                    public boolean isDisposed() {
                        return false;
                    }
                });*/
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        }, BackpressureStrategy.LATEST).doOnCancel(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("doOnCancel");
            }
        }).subscribe(new Subscriber<Integer>() {
            Subscription subscription;
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                subscription = s;
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
                if (integer == 1) {
                    /**
                     * 取消订阅后不会再接收到事件源发送的onNext(2)和onComplete,
                     * 但是事件源还是会继续执行完
                     */
                    subscription.cancel();//取消订阅
                }
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
