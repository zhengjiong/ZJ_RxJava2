package com.example.error;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * CreateTime:18/6/12  09:55
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example9RxJavaPlugin {

    public static void main(String[] args) {
        //test1();
        //test2();
        test3();
    }

    /**
     * onError后如果再次onError, Rx会抛出异常, 而且下面的观察者在onError是不能捕获的,
     * 这个时候可以使用RxJavaPlugins来捕获该异常, 程序也不会崩溃.
     * <p>
     * 输出:
     * onNext 1
     * onNext 2
     * onNext 3
     * RxJavaPlugins 111
     * RxJavaPlugins 222
     */
    private static void test1() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("RxJavaPlugins " + throwable.getMessage());
            }
        });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);

                e.onError(new NullPointerException("111"));
                e.onError(new NullPointerException("222"));
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
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
    }

    static Disposable disposable;

    /**
     * disposable.dispose()执行后,如果再e.onError, Rx会抛出异常, 而且下面的观察者在onError是不能捕获的,
     * 这个时候可以使用RxJavaPlugins来捕获该异常, 程序也不会崩溃.
     * <p>
     * 输出:
     * onNext 1
     * onNext 2
     * onNext 3
     * onNext disposable.dispose()
     * RxJavaPlugins 111
     * RxJavaPlugins 222
     */
    private static void test2() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("RxJavaPlugins " + throwable.getMessage());
            }
        });


        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onError(new NullPointerException("111"));
                e.onError(new NullPointerException("222"));
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
                if (integer == 3) {
                    System.out.println("onNext disposable.dispose()");
                    disposable.dispose();
                }
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
    }

    private static void test3() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("RxJavaPlugins " + throwable.getMessage());
            }
        });

        /**
         * 这里没有添加onError的Consumer, 按理说程序会崩溃的, 可以使用RxJavaPlugins捕获异常
         */
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("1");
                e.onNext("2");
                e.onNext("3");
                throw new NullPointerException("zhengjiong");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("onNext " + s);
            }
        });
    }
}
