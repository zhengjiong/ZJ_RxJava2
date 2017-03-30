package com.example.error;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by zj on 2017/3/30.
 */

public class Example3 {

    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);

                System.out.println("throw error1");
                System.out.println(1/0);
                //e.onError(new RuntimeException("error1"));
                System.out.println("throw error2");
                /**
                 * 注意:
                 * 多吃onError会导致程序崩溃!,
                 * 判断是否已经解除订阅, 解除订阅后讲不会再发送error
                 */

                if (!e.isDisposed()) {
                    e.onError(new RuntimeException("error2"));
                    throw new RuntimeException("error3");
                }
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
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
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
                /**
                 * onError的时候取消订阅, 但是如果上面继续onError, 程序还是会崩溃
                 */
                disposable.dispose();
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
