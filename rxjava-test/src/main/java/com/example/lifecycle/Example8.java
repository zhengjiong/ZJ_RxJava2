package com.example.lifecycle;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Title: Example6
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/4/2  22:53
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example8 {
    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    static BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();


    /**
     * 输出结果:
     * onNext 1
     * compositeDisposable.clear()
     * doOnDispose
     */
    public static void main(String[] args){
        compositeDisposable.add(behaviorSubject
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doOnDispose");
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        System.out.println("onNext " + integer);
                        if (1 == integer) {
                            System.out.println("compositeDisposable.clear()");
                            compositeDisposable.clear();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        System.out.println("onError" + throwable.toString());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("onComplete");
                    }
                }));

        behaviorSubject.onNext(1);
        behaviorSubject.onNext(2);
        behaviorSubject.onNext(3);
        behaviorSubject.onComplete();
    }

}
