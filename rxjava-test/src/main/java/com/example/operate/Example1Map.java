package com.example.operate;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Title: Example1Map
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/3/29  16:11
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example1Map {
    public static void main(String[] args) {
        Flowable.just("1")
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(@NonNull String s) throws Exception {
                        return Integer.valueOf(s);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println("onNext " + integer);
                    }
                });
    }
}
