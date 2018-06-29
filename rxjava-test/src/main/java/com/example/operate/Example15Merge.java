package com.example.operate;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * CreateTime:18/6/4  11:14
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example15Merge {

    /**
     * 在 Rx 操作符中，merge 的作用是把多个 Observable 结合起来，接受可变参数，也支持迭代器集合。
     * 注意它和 concat 的区别在于，不用等到 发射器 A 发送完所有的事件再进行发射器 B 的发送。
     */
    public static void main(String[] args) {
        //test1();
        test2();
    }

    /**
     * 输出并不是按照先第一个Observable发送然后第二个发送, 顺序是没有规律的
     *
     * 输出:
     * onNext -> 98
     * onNext -> 99
     * onNext -> 0
     * onNext -> 1
     * onNext -> 2
     * onNext -> 3
     * onNext -> 4
     * onComplete
     */
    private static void test1() {
        Observable.merge(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(100);
                    e.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io()), Observable.just(98, 99).subscribeOn(Schedulers.io())).subscribeOn(Schedulers.io())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext -> " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

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

    /**
     * 输出是按照先第一个Observable发送然后第二个发送,是有序的
     * 输出:
     * onNext -> 0
     * onNext -> 1
     * onNext -> 2
     * onNext -> 3
     * onNext -> 4
     * onNext -> 98
     * onNext -> 99
     * onComplete
     */
    private static void test2() {
        Observable.concat(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(100);
                    e.onNext(i);
                }
                //必须加onCompleted()方法, 下一个Observable才会发送, 去掉后98,99将不会发送
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()), Observable.just(98, 99).subscribeOn(Schedulers.io())).subscribeOn(Schedulers.io())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext -> " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

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
