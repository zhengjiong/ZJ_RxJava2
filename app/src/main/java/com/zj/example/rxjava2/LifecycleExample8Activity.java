package com.zj.example.rxjava2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by zhengjiong
 * date: 2018/5/30 22:05
 */

public class LifecycleExample8Activity extends AppCompatActivity {
    DisposableObserver disposableObserver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //subscribeWith订阅的源码是把Observer对象同时返回,正好配合DisposableObserver:
        disposableObserver = Observable.just(true)
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //需要取消订阅的地方：
        disposableObserver.dispose();
    }
}
