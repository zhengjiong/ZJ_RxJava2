package com.example.operate;

import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Title: Example6Empty
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/3/29  17:33
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example6Empty {

    /**
     * 输出:
     * onNext empty
     * onComplete
     */
    public static void main(String[] args) {
        Flowable.<String>empty()
                .switchIfEmpty(new Flowable<String>() {
                    @Override
                    protected void subscribeActual(Subscriber<? super String> s) {
                        s.onNext("empty");
                        s.onComplete();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        System.out.println("onNext " + s);
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
