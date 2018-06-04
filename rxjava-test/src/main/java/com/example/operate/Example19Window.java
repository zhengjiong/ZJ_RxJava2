package com.example.operate;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * CreateTime:18/6/4  14:21
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example19Window {

    /**
     * window,按照实际划分窗口，将数据发送给不同的 Observable
     */
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        /**
         * 把0,1分给第一个观察者, 2,3分给后面一个,4,5再后面一个
         * 输出:
         longObservable onNext 0
         longObservable onNext 1
         longObservable onComplete
         longObservable onNext 2
         longObservable onNext 3
         longObservable onComplete
         longObservable onNext 4
         longObservable onNext 5
         longObservable onComplete
         onComplete
         */
        Observable.interval(0, 1, TimeUnit.SECONDS)// 间隔一秒发射一次
                .take(6)
                .window(2)
                .subscribe(new Observer<Observable<Long>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Observable<Long> longObservable) {
                        longObservable.subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                System.out.println("longObservable onNext " + aLong);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                System.out.println("onError " + throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                System.out.println("longObservable onComplete");
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });


        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
