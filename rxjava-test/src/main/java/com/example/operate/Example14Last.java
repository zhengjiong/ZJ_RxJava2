package com.example.operate;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * CreateTime:18/6/4  11:02
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example14Last {


    public static void main(String[] args) {
        test1();
        //test2();
    }

    /**
     * last 操作符仅取出可观察到的最后一个值，或者是满足某些条件的最后一项。
     *
     * 输出:
     * onSuccess -> 9
     */
    private static void test1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                //如果这里不onNext就会输出last中的值
                e.onComplete();
            }
            //默认值9
        }).last(9)
                .subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer integer) {
                System.out.println("onSuccess -> " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError " + e.getMessage());
            }
        });
    }

    /**
     * last 操作符仅取出可观察到的最后一个值，或者是满足某些条件的最后一项。(这里是最后一个值)
     * 输出:
     * onSuccess -> 3
     */
    private static void test2() {
        Observable.just(1, 2, 3)
                .last(9)//默认值9
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        System.out.println("onSuccess -> " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.getMessage());
                    }
                });
    }
}
