package com.example.lifecycle;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.SafeObserver;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Title: Example9
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/4/2  22:53
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example10 {
    static BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();

    /**
     * 输出:
     * onNext 1
     * disposable.dispose()
     * doOnDispose run
     *
     * 和Example9差不多 只是不使用CompositeDisposable, 而是用自己返回的Disposable来取消订阅
     * 注意: 如果要使用subscribeWith,必须使用SafeObserver!
     */
    public static void main(String[] args){
        behaviorSubject
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doOnDispose run");
                    }
                })
                //使用SafeObserver
                .subscribeWith(new SafeObserver<Integer>(new Observer<Integer>() {
                    Disposable disposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext " + integer);
                        if (1 == integer) {
                            System.out.println("disposable.dispose()");
                            disposable.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                }));

        behaviorSubject.onNext(1);
        behaviorSubject.onNext(2);
        behaviorSubject.onNext(3);
        behaviorSubject.onComplete();
    }

}
