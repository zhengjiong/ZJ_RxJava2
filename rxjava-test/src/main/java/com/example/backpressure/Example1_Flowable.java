package com.example.backpressure;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zj on 2017/4/4.
 */

public class Example1_Flowable {
    /**
     * 安卓测试效果看app项目中的com.zj.example.rxjava2.backpressure.FlowableExample
     */
    public static void main(String[] args) {

        /**
         * 这里必须使用Schedulers.newThread(),让事件源和订阅者运行在不同的线程上才不会造成线程堵塞而影响测试结果,
         * 最后还需要加上System.in.read();才会有打印输出
         *
         * 输出:
         * onNext 126 ,RxNewThreadScheduler-1
         * onNext 127 ,RxNewThreadScheduler-1
         * onNext 392439550 ,RxNewThreadScheduler-1
         * onNext 392439551 ,RxNewThreadScheduler-1
         */
        Flowable.create(new FlowableOnSubscribe<Long>() {
            @Override
            public void subscribe(final FlowableEmitter<Long> e) throws Exception {
                for (long i = 0; i < Long.MAX_VALUE; i++) {
                    e.onNext(i);
                }
                e.onComplete();
            }
        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                Thread.sleep(50);
                System.out.println("onNext " + aLong + " ," + Thread.currentThread().getName());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                System.out.println("onError " + throwable.toString());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("oncomplete");
            }
        });

        try {
            System.in.read();//必须要加上这一句才会有输出
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
