package com.example.operate;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Title: Example2From
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/3/29  16:25
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example2From {

    public static void main(String[] args){
        test1();
        test2();
        test3();
    }

    /**
     * 输出:
     * accept 1
     * accept 2
     * accept 3
     */
    private static void test3() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        Flowable.just(list1)
                .flatMap(new Function<List<Integer>, Publisher<Integer>>() {
                    @Override
                    public Publisher<Integer> apply(@NonNull List<Integer> integers) throws Exception {
                        return Flowable.fromIterable(integers);
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println("accept " + integer);
            }
        });
    }

    /**
     * 输出:
     * 1
     * 2
     * 3
     */
    private static void test2() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        Flowable.fromIterable(list1).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    /**
     * 输出:
     * [1, 2, 3]
     */
    private static void test1() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        Flowable.fromArray(list1).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(@NonNull List<Integer> integers) throws Exception {
                System.out.println(integers.toString());
            }
        });
    }
}
