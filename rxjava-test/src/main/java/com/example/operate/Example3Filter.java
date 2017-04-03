package com.example.operate;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Title: Example3Filter
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/3/29  16:34
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example3Filter {


    public static void main(String[] args){
        test1();

    }

    /**
     * 输出:
     * 6
     * 7
     * 8
     * 9
     */
    private static void test1() {
        //just,和fromArray效果一样
        Flowable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9)
        //Flowable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        if (integer > 5) {
                            return true;
                        }
                        return false;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });
    }
}
