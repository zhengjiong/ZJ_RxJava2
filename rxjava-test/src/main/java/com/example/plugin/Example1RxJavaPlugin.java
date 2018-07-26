package com.example.plugin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Company: 上加下信息技术成都有限公司
 * CreateTime:18/7/18  08:25
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example1RxJavaPlugin {

    static class WrapObserver<T> implements Observer<T> {
        Observer actual;

        public WrapObserver(Observer actual) {
            System.out.println("WrapObserver constructor invoke");
            this.actual = actual;
        }

        @Override
        public void onSubscribe(Disposable d) {
            System.out.println("hook onSubscribe");
            actual.onSubscribe(d);
        }

        @Override
        public void onNext(T t) {
            System.out.println("hook onNext " + t);
            /**
             * 这里必须调用actual.onNext(t), 不然下游会收不到
             */
            actual.onNext(t);
        }

        @Override
        public void onError(Throwable e) {
            System.out.println("onError " + e);
            /**
             * 这里必须调用actual.onError(t), 不然下游会收不到
             */
            actual.onError(e);
        }

        @Override
        public void onComplete() {
            System.out.println("hook onComplete");
            /**
             * 这里必须调用actual.onComplete(t), 不然下游会收不到
             */
            actual.onComplete();
        }
    }

    public static void main(String[] args) {
        RxJavaPlugins.setOnObservableSubscribe(new BiFunction<Observable, Observer, Observer<Integer>>() {
            @Override
            public Observer<Integer> apply(Observable observable, Observer observer) throws Exception {
                return new WrapObserver<>(observer);
            }
        });

        Observable.just(1).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("onNext " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("onError " + throwable);
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("onComplete");
            }
        });

    }
}
