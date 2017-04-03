package com.example.lifecycle;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Title: Example1
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/4/3  15:27
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example1_Flowable {


    public static void main(String[] args) {
        test1();
    }

    /**
     * doOnCancel相当于Observable的doOnDispose方法
     * <p>
     * 注意:
     * 经过测试发现:doOnCancel执行的前提是必须手动执行了取消订阅方法,才会进入!
     * 正常onComplete之后是不会进入的,必须执行dispose,或者clear等方法才行!
     * <p>
     * 输出:
     * onNext 1
     * onNext 2
     * onNext 3
     * onComplete
     */
    private static void test1() {
        /**
         * doOnCancel相当于Observable的doOnDispose方法
         */
        Flowable.just(1, 2, 3).doOnCancel(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("doOnCancel");
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println("onNext " + integer);
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
