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
public class Example4_Flowable {

    public static void main(String[] args){
        test1();
    }

    /**
     * 输出:
     * dispose
     * onNext1
     * onNext2
     * onNext3
     * onComplete
     * cancel
     */
    private static void test1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                /**
                 * 注意:
                 * 如果使用Flowable,就不要使用setDisposable,应该使用setCancellable,
                 * 如果要使用setDisposable, 就不要使用setCancellable, 两者
                 * 只能选其一,不然直接默认回调第一个
                 *
                 */
                e.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        /**
                         * 会直接打印出来,不管有没有取消都会打印!!!
                         * 如果使用Flowable,就不要使用setDisposable,应该使用setCancellable!
                         * 如果要使用setDisposable, 就不要使用setCancellable, 两者
                         * 只能选其一,不然直接默认回调第一个
                         */
                        System.out.println("dispose");
                    }

                    @Override
                    public boolean isDisposed() {
                        return false;
                    }
                });
                e.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        /**
                         * 当onComplete执行后,会自动解除订阅,或者手动执行dipose方法也可以,就会进入此方法,
                         * 如果不执行onComplete或者onError或dipose就不会解除订阅!
                         */
                        System.out.println("cancel");
                    }
                });
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                /**
                 * 如果不执行onComplete或者onError
                 * 就不会解除订阅!
                 */
                e.onComplete();
            }
        }, BackpressureStrategy.LATEST).doOnCancel(new Action() {
            @Override
            public void run() throws Exception {
                /**
                 * 正常onCompleted不会进入这个方法, 必须要手动调用dispose方法才会进入, 看demo5
                 */
                System.out.println("doOnCancel");
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext" + integer);
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
