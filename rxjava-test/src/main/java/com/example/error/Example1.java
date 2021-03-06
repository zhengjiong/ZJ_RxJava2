package com.example.error;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

/**
 * Created by zj on 2017/3/30.
 */

public class Example1 {

    public static void main(String[] args) {
        test1();
        test2();
    }


    private static void test1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        //代表已经解除订阅
                        System.out.println("dispose");
                    }

                    @Override
                    public boolean isDisposed() {
                        return false;
                    }
                });
                e.onNext(1);
                e.onNext(2);
                e.onComplete();//会导致解除订阅

                System.out.println("e.isDisposed() = " +e.isDisposed());
                /**
                 * 注意: 和RxJava1的区别:
                 * RxJava2解除订阅后再次onError会导致程序崩溃!
                 */
                e.onError(new RuntimeException("error1"));
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
                System.out.println("onError " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    private static void test2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        //代表已经解除订阅
                        System.out.println("dispose");
                    }

                    @Override
                    public boolean isDisposed() {
                        return false;
                    }
                });
                e.onNext(1);
                e.onNext(2);
                e.onError(new RuntimeException("error-1"));//会导致解除订阅

                System.out.println("e.isDisposed() = " +e.isDisposed());
                /**
                 * 注意: 和RxJava1的区别:
                 * RxJava2解除订阅后再次onError会导致程序崩溃!
                 * 这里可以加上if判断是否已经取消订阅,不会就会导致崩溃
                 */
                if (!e.isDisposed()) {
                    e.onError(new RuntimeException("error-2"));
                }
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
                System.out.println("onError " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
