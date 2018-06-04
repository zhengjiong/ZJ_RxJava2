package com.example.operate;

import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * CreateTime:18/6/4  09:14
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example10Concat {

    public static void main(String[] args) {
        //test1();
        test2();
    }

    /**
     * concat串联连接两个Observable
     *
     * 输出:
     * onNext -> 1
     * onNext -> 2
     * onNext -> 3
     * onNext -> 3
     * onNext -> 5
     * onComplete
     */
    private static void test1() {
        Observable.concat(Observable.just(1, 2, 3), Observable.just(3, 5)).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext -> " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError -> " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    /**
     * 输出:
     * onSuccess -> 1
     *
     * 关键是下面的firstElement()方法, 执行这个方法后,如果第一个Observable发出一个对象, 那第二个
     * Observable根本不会执行, 要想执行第二个Observable, 可以屏蔽掉e.onNext(1)和e.onNext(2).
     */
    private static void test2() {
        Observable.concat(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                //要响执行第二个Observable
                e.onNext(1);
                e.onNext(2);
                e.onComplete();
            }
        }), Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(3);
                System.out.println("Observable2 onNext3");
                e.onComplete();
            }
        })).firstElement().subscribe(new MaybeObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer integer) {
                System.out.println("onSuccess -> " + integer);
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
