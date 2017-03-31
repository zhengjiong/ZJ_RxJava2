package com.zj.example.rxjava2;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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
import io.reactivex.schedulers.Schedulers;

/**
 * Title: LifecycleExample1Activity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/3/30  14:00
 *
 * @author 郑炯
 * @version 1.0
 */
public class LifecycleExample3Activity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Subscription subscription;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 此demo是反应错误的情况
         */
        compositeDisposable.add(Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                /**
                 * 注意:
                 * 通过执行Flowable.create创建的FlowableOnSubscribe, 执行
                 * setDisposable不是在取消订阅的时候执行而是一订阅就立刻执行!
                 */
                e.setDisposable(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        System.out.println("setDisposable onDispose " + Thread.currentThread().getName());
                    }
                });
                e.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        System.out.println("setCancellable cancel");
                    }
                });
                System.out.println("onNext 1");
                e.onNext(1);
                SystemClock.sleep(2000);
                System.out.println("onNext 2");
                e.onNext(2);
                SystemClock.sleep(2000);
                System.out.println("onNext 3");
                e.onNext(3);

            }
        }, BackpressureStrategy.BUFFER).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println("onNext -> accept " + integer);
                if (integer == 2) {
                    //subscription.cancel();
                }
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
                LifecycleExample3Activity.this.subscription = subscription;
            }
        }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
