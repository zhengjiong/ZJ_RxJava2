package com.example.lifecycle;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zj on 2017/3/30.
 */

public class Example0 {

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
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        //onComplete执行后, 会进入此方法, 代表已经解除订阅
                        System.out.println("dispose");
                    }
                    @Override
                    public boolean isDisposed() {
                        return false;
                    }
                });
                e.onNext(1);
                e.onNext(2);
                e.onComplete();//执行后会解除订阅

                /**
                 * 会执行下面这行代码, 但是不会在订阅者中执行onNext3,因为已经onCompleted了, 就已经取消订阅了
                 */
                System.out.println("send 3");
                e.onNext(3);

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

    private static void test2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        // throw new RuntimeException执行后, 会进入此方法, 代表已经解除订阅
                        System.out.println("dispose");
                    }
                    @Override
                    public boolean isDisposed() {
                        return false;
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
                System.out.println("send 3");
                e.onNext(3);

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
