package com.example.simple;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Example1 {

    public static void main(String[] args) {
        /**
         * 创建观察者
         */
        Observer observer = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(Object o) {
                System.out.println("onNext " + o);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        /**
         * 创建被观察者
         */
        Observable observable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                //e.onError(new RuntimeException("custom error"));
                e.onComplete();
                e.onNext(4);
            }
        });

        observable.subscribe(observer);
    }
}