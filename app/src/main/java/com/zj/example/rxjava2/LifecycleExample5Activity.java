package com.zj.example.rxjava2;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Title: LifecycleExample5Activity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/4/5  20:22
 *
 * @author 郑炯
 * @version 1.0
 */
public class LifecycleExample5Activity extends AppCompatActivity {
    HashMap<String, CompositeDisposable> hashMap = new HashMap<>();
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle_example5);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 5; i++) {
                    SystemClock.sleep(300);
                    test1(i);
                }
            }
        });
    }

    public void test1(final int i) {
        CompositeDisposable compositeDisposable = hashMap.get("test1");
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
            hashMap.put("test1", compositeDisposable);
        } else {
            System.out.println("clear " + i);
            compositeDisposable.clear();
            //hashMap.remove("test1");
        }
        compositeDisposable.add(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        System.out.println("dispose " + i);
                    }

                    @Override
                    public boolean isDisposed() {
                        return false;
                    }
                });
                SystemClock.sleep(3000);
                System.out.println("onNext " + i + " ,thread=" + Thread.currentThread().getName());
                e.onNext(i);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println("onNext " + integer + " ,thread=" + Thread.currentThread().getName());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                System.out.println("onError " + throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("onComplete");
            }
        }));
    }
}
