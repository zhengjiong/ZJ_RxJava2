package com.example.lifecycle;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Title: Example7
 * Description:
 * Copyright:Copyright(c)2016
 * <p>
 * CreateTime:17/4/2  22:53
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example7_Flowable {
    static CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * 输出:
     * onNext 1
     * size = 0
     * onNext 2
     * onNext 3
     * <p>
     * 此demo是错误的写法, 正确方式看demo8_Flowable
     */
    public static void main(String[] args) {
        compositeDisposable.add(Flowable.just(1, 2, 3)
                .doOnCancel(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doOnCancel");
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println("onNext " + integer);
                        if (integer == 1) {
                            System.out.println("size = " + compositeDisposable.size());
                            /**
                             * 这样是错误的写法, 因为上面打印size=0, 这个时候还没有add进去,
                             * 正确的方式看Example8_Flowable
                             */
                            compositeDisposable.dispose();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        System.out.println("onError");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("onComplete");
                    }
                }));
    }
}
