package com.zj.example.rxjava2;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscriptions.AsyncSubscription;
import io.reactivex.internal.subscriptions.ScalarSubscription;
import io.reactivex.schedulers.Schedulers;

/**
 * Title: LifecycleExample1Activity
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/3/30  14:00
 *
 * @author 郑炯
 * @version 1.0
 */
public class LifecycleExample4Activity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Subscription subscription;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable.add(new Flowable<Integer>() {
            @Override
            protected void subscribeActual(Subscriber<? super Integer> s) {
                s.onSubscribe(new AsyncSubscription(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        /**
                         * 当执行compositeDisposable.clear()之后, 会进入此方法
                         */
                        System.out.println("setDisposable onDispose " + Thread.currentThread().getName());
                    }
                }));
                System.out.println("onNext 1");
                s.onNext(1);
                SystemClock.sleep(2000);
                System.out.println("onNext 2");
                s.onNext(2);
                SystemClock.sleep(2000);
                System.out.println("onNext 3");
                s.onNext(3);
            }
        }.observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println("onNext -> accept " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                System.out.println("onError -> accept");
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("onComplete");
            }
        }, new Consumer<Subscription>() {
            @Override
            public void accept(@NonNull Subscription subscription) throws Exception {
                /**
                 * 当subscribe订阅的时候回立刻进入此方法
                 */
                System.out.println("onSubscribe accept");
                LifecycleExample4Activity.this.subscription = subscription;
            }
        }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
