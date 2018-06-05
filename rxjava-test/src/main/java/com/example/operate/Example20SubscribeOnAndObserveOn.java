package com.example.operate;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * CreateTime:18/6/5  09:21
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example20SubscribeOnAndObserveOn {

    /**
     * RxJava线程切换总结:
     * 1. subscribeOn的调用切换该方法执行之前的线程。
     * 2. observeOn的调用, 切换该方法执行之后的线程。
     * 3. observeOn之后，不可再调用subscribeOn 切换线程
     */
    public static void main(String[] args) {
        test1();
    }

    /**
     * 输出:
     * subscribe  , Thread[RxNewThreadScheduler-1,5]
     * map 1 ,      Thread[RxSingleScheduler-1,5]
     * onNext 1 ,   Thread[RxNewThreadScheduler-2,5]
     * onComplete  ,Thread[RxNewThreadScheduler-2,5]
     */
    private static void test1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                System.out.println("subscribe " + " , " + Thread.currentThread().toString());
                e.onNext(1);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.single()).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                System.out.println("map " + integer + " , " + Thread.currentThread().toString());
                return integer;
            }
        }).observeOn(Schedulers.newThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer + " , " + Thread.currentThread().toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("onComplete " + " , " + Thread.currentThread().toString());
            }
        });

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
