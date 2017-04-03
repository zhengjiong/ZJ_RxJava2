package com.example.lifecycle;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
public class Example2 {


    public static void main(String[] args) {
        test1();
    }

    /**
     * 注意:
     * 经过测试发现:doOnDispose执行的前提是必须手动执行了取消订阅方法,才会进入!
     * 正常onComplete之后是不会进入的,必须执行dispose,或者clear等方法才行!
     *
     * 输出:
     * onNext 1
     * onNext 2
     * onNext 3
     * onComplete
     */
    private static void test1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("doOnDispose");
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
