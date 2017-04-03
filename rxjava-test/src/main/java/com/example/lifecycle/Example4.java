package com.example.lifecycle;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

/**
 * Title: Example4
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/3/30  13:44
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example4 {

    public static void main(String[] args){
        test1();
    }

    /**
     * 输出:
     * onSubscribe
     * onNext 1
     * onNext 2
     * onComplete
     * dispose
     */
    private static void test1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        /**
                         * 当onComplete执行后,会自动解除订阅,或者手动执行dipose方法也可以,就会进入此方法,
                         * 如果不执行onComplete或者onError或dipose就不会解除订阅!
                         */
                        System.out.println("dispose");
                    }

                    @Override
                    public boolean isDisposed() {
                        /**
                         * 不会进入这个方法
                         */
                        System.out.println("isDisposed");
                        return false;
                    }
                });
                e.onNext(1);
                e.onNext(2);
                /**
                 * 如果不执行onComplete或者onError
                 * 就不会解除订阅!
                 */
                e.onComplete();
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                /**
                 * 正常onCompleted不会进入这个方法, 必须要手动调用dispose方法才会进入, 看demo5
                 */
                System.out.println("doOnDispose");
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
