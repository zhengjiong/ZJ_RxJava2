package com.zj.example.rxjava2.backpressure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zj on 2017/4/4.
 */

public class ObservableExample extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 如果在RxJava1中，内存数据超过128个时将会抛出MissingBackpressureException错误；
         * 而在RxJava2中并不会报错，数据会一直放到内存中，直到发生OutOfMemoryError。如下:
         *
         *  Caused by: java.lang.OutOfMemoryError: Failed to allocate a 16 byte allocation with 4182728 free bytes and 17MB until OOM; failed due to fragmentation (required continguous free 4096 bytes for a new buffer where largest contiguous free 0 bytes)
         *  04-04 10:43:24.479 11009-11009/com.zj.example.rxjava2 W/System.err:     at com.zj.example.rxjava2.LifecyclerRootActivity$5.accept(LifecyclerRootActivity.java:82)
         *
         */
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                for (long i = 0; i < Long.MAX_VALUE; i++) {
                    e.onNext(i);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
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
