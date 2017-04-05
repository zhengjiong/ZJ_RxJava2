package com.example.error;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Title: Example8
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/4/5  09:13
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example8 {

    /**
     * 解决在onNext中抛出异常的两种方式:
     * 方式1:看Example7中的:test2()
     * 方式2:看Example8
     */
    public static void main(String[] args){
        /**
         * RxJava2允许在Consumer作为onNext的时候抛出异常,
         */
        Observable.just(1, 2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println("onNext " + integer);
                if (integer == 1) {
                    throw new RuntimeException("error-1");
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                System.out.println("onError " + throwable.toString());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("onComplete");
            }
        });
    }
}
