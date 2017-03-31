package com.zj.example.rxjava2;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
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
public class LifecycleExample1Activity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable.add(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                System.out.println("e.isDisposed()" + e.isDisposed());
                e.onNext(1);
                SystemClock.sleep(2000);
                System.out.println("e.isDisposed()" + e.isDisposed());
                e.onNext(2);
                SystemClock.sleep(2000);
                System.out.println("e.isDisposed()" + e.isDisposed());
                e.onNext(3);
            }
        }).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe(new Consumer<Integer>() {
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
        }, new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                System.out.println("onSubscribe -> accept");
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
