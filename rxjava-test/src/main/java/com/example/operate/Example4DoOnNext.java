package com.example.operate;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Title: Example4DoOnNext
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/3/29  16:41
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example4DoOnNext {

    public static void main(String[] args) {
        test1();
    }

    /**
     * 输出:
     * doOnNext accept 1
     * onNext 1
     * doOnNext accept 2
     * onNext 2
     * onComplete
     */
    private static void test1() {
        Flowable.just(1, 2)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println("doOnNext accept " + integer);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println("onNext " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("onComplete ");
                    }
                });
    }
}
