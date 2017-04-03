package com.example.lifecycle;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Title: Example6
 * Description:
 * Copyright:Copyright(c)2016
 * CreateTime:17/4/2  22:53
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example6 {
    static CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void main(String[] args) {
        Test1();
    }

    /**
     * 此demo是错误的写法, 正确方式看demo8
     * 解除订阅后都还能接收到onNext消息!!!
     */
    private static void Test1() {
        compositeDisposable.add(Observable.create(new ObservableOnSubscribe<Integer>() {
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
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (integer == 1) {
                    System.out.println("compositeDisposable.clear compositeDisposable.size=" + compositeDisposable.size());
                    /**
                     * 这样是错误的写法, 因为上面打印size=0, 这个时候还没有add进去,
                     * 正确的方式看Example8
                     */
                    compositeDisposable.clear();
                }
                System.out.println("compositeDisposable.isDisposed() = " + compositeDisposable.isDisposed());
                System.out.println("onNext " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable e) throws Exception {
                System.out.println("onError " + e.toString());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("onComplete");
            }
        }));
    }

}
