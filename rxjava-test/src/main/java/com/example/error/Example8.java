package com.example.error;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Title: Example10
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/4/2  18:30
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example8 {

    public static void main(String[] args){
        test1();
    }

    private static void test2() {

    }

    private static void test1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                if (true) {
                    throw new RuntimeException("exception-1");
                }
                System.out.println("error-2");
                /**
                 * RxJava1和RxJava2的区别之一:
                 * RxJava只能onError一次, 之后再发生异常就会报错
                 */
                e.onError(new RuntimeException("error-2"));
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
                System.out.println("onError " + e.toString());
                throw new UnsupportedOperationException();
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
