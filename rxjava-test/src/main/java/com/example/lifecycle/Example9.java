package com.example.lifecycle;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.SafeObserver;
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
public class Example9 {
    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    static BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();

    /**
     * 注意: 如果要使用subscribeWith,必须使用DisposableObserver或者SafeObserver!
     *
     * 下面的MyObserver有问题:compositeDisposable.clear执行后还会继续接受到事件源
     * 发来的事件(解除订阅后还会调用onNext), 需要使用DisposableObserver!
     */
    public static void main(String[] args){
        compositeDisposable.add(behaviorSubject
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doOnDispose run");
                    }
                })
                //使用DisposableObserver
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext " + integer);
                        if (1 == integer) {
                            System.out.println("compositeDisposable.dispose() " + " size=" + compositeDisposable.size());
                            compositeDisposable.clear();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                }));
                //使用SafeObserver
                /*.subscribeWith(new SafeObserver<Integer>(new Observer<Integer>() {
                    Disposable disposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext " + integer);
                        if (1 == integer) {
                            System.out.println("compositeDisposable.dispose() " + " size=" + compositeDisposable.size());
                            compositeDisposable.clear();
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
                })));*/

        behaviorSubject.onNext(1);
        behaviorSubject.onNext(2);
        behaviorSubject.onNext(3);
        behaviorSubject.onComplete();
    }

    /**
     * 这个是错误的!
     * 不能用MyObserver, 不然compositeDisposable.clear()执行后,
     * onNext还能继续收到事件源发来得消息!需要使用SafeObserve!
     */
    static class MyObserver implements Observer<Integer> , Disposable{

        @Override
        public void onSubscribe(Disposable d) {
            System.out.println("onSubscribe");
        }

        @Override
        public void onNext(Integer integer) {
            System.out.println("onNext " + integer);
            if (1 == integer) {
                System.out.println("compositeDisposable.dispose() " + " size=" + compositeDisposable.size());
                compositeDisposable.clear();
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

        @Override
        public void dispose() {
            System.out.println("dispose");
        }

        @Override
        public boolean isDisposed() {
            return false;
        }
    }
}
