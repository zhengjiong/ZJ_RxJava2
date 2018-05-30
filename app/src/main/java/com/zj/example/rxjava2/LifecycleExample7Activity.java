package com.zj.example.rxjava2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.internal.observers.DisposableLambdaObserver;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by zhengjiong
 * date: 2018/5/30 22:02
 */

public class LifecycleExample7Activity extends AppCompatActivity {

    private DisposableObserver<Boolean> disposableObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //DisposableObserver即实现了Observer，又实现了Disposable接口
        disposableObserver = new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        Observable.just(true).subscribe(disposableObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在需要取消订阅的地方对这个observer进行取消订阅即可。
        disposableObserver.dispose();
    }
}
