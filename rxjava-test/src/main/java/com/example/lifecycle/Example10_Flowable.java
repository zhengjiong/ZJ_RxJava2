package com.example.lifecycle;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.SafeObserver;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.SafeSubscriber;

/**
 * Title: Example9
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/4/2  22:53
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example10_Flowable {
    static BehaviorProcessor<Integer> behaviorSubject = BehaviorProcessor.create();

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
                .doOnCancel(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doOnCancel run");
                    }
                })
                .subscribeWith(new Subscriber<Integer>() {
                    Subscription subscription;
                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext " + integer);
                        if (1 == integer) {
                            System.out.println("subscription.cancel()");
                            subscription.cancel();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });

        behaviorSubject.onNext(1);
        behaviorSubject.onNext(2);
        behaviorSubject.onNext(3);
        behaviorSubject.onComplete();
    }

}
