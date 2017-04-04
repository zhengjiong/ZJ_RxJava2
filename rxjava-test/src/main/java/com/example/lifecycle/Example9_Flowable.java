package com.example.lifecycle;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.subscribers.DefaultSubscriber;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Title: Example6
 * Description:
 * Copyright:Copyright(c)2016
 * <p>
 * CreateTime:17/4/2  22:53
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example9_Flowable {
    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    static BehaviorProcessor<Integer> behaviorSubject = BehaviorProcessor.create();

    /**
     * 注意: 如果要使用subscribeWith,必须使用DisposableSubscriber!
     * <p>
     * 下面的MyObserver有问题:compositeDisposable.clear执行后还会继续接受到事件源
     * 发来的事件(解除订阅后还会调用onNext), 需要使用DisposableSubscriber!
     */
    public static void main(String[] args) {
        compositeDisposable.add(behaviorSubject
                .doOnCancel(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doOnCancel run");
                    }
                })
                //.subscribeWith(new MyObserver()));
                //使用DisposableSubscriber或者DefaultSubscriber或者ResourceSubscriber
                .subscribeWith(new DisposableSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext " + integer);
                        if (1 == integer) {
                            System.out.println("compositeDisposable.dispose() " + " size=" + compositeDisposable.size());
                            compositeDisposable.clear();
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
                }));

        behaviorSubject.onNext(1);
        behaviorSubject.onNext(2);
        behaviorSubject.onNext(3);
        behaviorSubject.onComplete();
    }

    /**
     * 这个是错误的!
     * 不能用MyObserver, 不然compositeDisposable.clear()执行后,
     * onNext还能继续收到事件源发来得消息!需要使用DisposableSubscriber!
     */
    static class MyObserver implements Subscriber<Integer>, Disposable {

        @Override
        public void onSubscribe(Subscription s) {
            System.out.println("onSubscribe");
            s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(Integer integer) {
            System.out.println("onNext " + integer);
            if (1 == integer) {
                System.out.println("compositeDisposable.dispose() " + " size=" + compositeDisposable.size());
                /**
                 * clear之后还是会继续受到事件源发来得消息, 未找到原因!
                 */
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
