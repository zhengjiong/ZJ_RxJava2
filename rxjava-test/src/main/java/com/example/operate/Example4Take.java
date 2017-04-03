package com.example.operate;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Title: Example4Take
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/3/29  16:38
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example4Take {


    public static void main(String[] args) {
        test1();
    }

    /**
     * 输出:
     * <p>
     * onNext accept 1
     * onNext accept 2
     * onNext accept 3
     */
    private static void test1() {
        Flowable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                //take 用于指定订阅者最多收到多少数据。
                .take(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println("onNext accept " + integer);
                    }
                });
    }
}
