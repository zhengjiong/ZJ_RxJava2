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
public class Example5 {

    public static void main(String[] args){
        test1();
    }

    /**
     * 输出:
     * onSubscribe
     * onNext 1
     * doOnDispose
     * dispose
     */
    private static void test1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        System.out.println("dispose");
                    }

                    @Override
                    public boolean isDisposed() {
                        return false;
                    }
                });
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                /**
                 * 正常onCompleted不会进入这个方法, 必须要手动调用dispose方法才会进入
                 */
                System.out.println("doOnDispose");
            }
        }).subscribe(new Observer<Integer>() {
            Disposable disposable;
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
                if (integer == 1) {
                    /**
                     * 取消订阅后不会再接收到事件源发送的onNext(2)和onComplete,
                     * 但是事件源还是会继续执行完
                     */
                    disposable.dispose();//取消订阅
                }
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
