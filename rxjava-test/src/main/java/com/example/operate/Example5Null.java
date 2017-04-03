package com.example.operate;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Title: Example5Null
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/3/29  17:19
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example5Null {

    public static void main(String[] args){

        test1();
    }

    /**
     * RxJava2.0 和 1.x 不一样， 所有的操作符都不能接收 null
     * 输出:
     * java.lang.NullPointerException: The item is null,main(Example5Null.java:31)
     */
    private static void test1() {
        Flowable.just(null)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        System.out.println("onNext");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        System.out.println("onError " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("onComplete");
                    }
                });
    }
}
