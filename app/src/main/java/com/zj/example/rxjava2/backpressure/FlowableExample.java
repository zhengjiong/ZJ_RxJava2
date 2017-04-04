package com.zj.example.rxjava2.backpressure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zj on 2017/4/4.
 */

public class FlowableExample extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 输出如下:
         *
         * onNext 125 ,main
         * onNext 126 ,main
         * onNext 127 ,main
         * onNext 183977429 ,main
         * onNext 183977430 ,main
         *
         * 对于flowable, 在创建时我们设定了FlowableEmitter.BackpressureMode.DROP，
         * 一开始他会输出0,1,2,3....127但之后会忽然跳跃到183977429,183977430,183977431 ...。
         * 中间的部分数据由于缓存不了，被抛弃掉了。
         * 经过试验drop和lastest效果一样
         */
        Flowable.create(new FlowableOnSubscribe<Long>() {
            @Override
            public void subscribe(final FlowableEmitter<Long> e) throws Exception {
                for (long i = 0; i < Long.MAX_VALUE; i++) {
                    e.onNext(i);
                }
                e.onComplete();
            }
        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                Thread.sleep(100);
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
    }
}
