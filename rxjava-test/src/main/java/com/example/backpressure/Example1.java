package com.example.backpressure;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zj on 2017/4/4.
 */

public class Example1 {

    /**
     * java项目很难造成内存溢出, 因为电脑内存太大, 要看内存溢出安卓测试效
     * 果看app项目中的com.zj.example.rxjava2.backpressure.ObservableExample;
     */
    public static void main(String[] args) {
        /**
         *
         * 他会按照0,1,2,3,4...的顺序依次消费，并输出log，而没有消费的数据将会都存在内存中。
         * 如果在RxJava1中，内存数据超过128个时将会抛出MissingBackpressureException错误；
         * 而在RxJava2中并不会报错，数据会一直放到内存中，直到发生OutOfMemoryError。
         */
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(final ObservableEmitter<Long> e) throws Exception {
                for (long i = 0; i < Long.MAX_VALUE; i++) {
                    e.onNext(i);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("onNext " + aLong);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError " + e.toString());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });

        try {
            System.in.read();//必须要加上这一句才会有输出
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
