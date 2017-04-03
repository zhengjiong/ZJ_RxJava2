package com.example.lifecycle;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.BehaviorProcessor;

/**
 * Title: Example6
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/4/2  22:53
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example8_Flowable {
    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    static BehaviorProcessor<Integer> behaviorProcessor = BehaviorProcessor.create();


    /**
     * 输出结果:
     * onNext 1
     * compositeDisposable.clear()
     * doOnCancel
     */
    public static void main(String[] args){
        compositeDisposable.add(behaviorProcessor
                .doOnCancel(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doOnCancel");
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

        behaviorProcessor.onNext(1);
        behaviorProcessor.onNext(2);
        behaviorProcessor.onNext(3);
        behaviorProcessor.onComplete();
    }

}
