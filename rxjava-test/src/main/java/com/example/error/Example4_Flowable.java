package com.example.error;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by zj on 2017/3/30.
 */

public class Example4_Flowable {

    public static void main(String[] args) {
        BehaviorProcessor<Integer> behaviorProcessor = BehaviorProcessor.create();

        behaviorProcessor.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println("onNext " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                System.out.println("onError " + throwable.toString());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("oncomplete");
            }
        });


        behaviorProcessor.onNext(1);
        behaviorProcessor.onNext(2);
        behaviorProcessor.onError(new RuntimeException("error-1"));
        /**
         * 两次onError会导致程序崩溃
         */
        //behaviorProcessor.onError(new RuntimeException("error-2"));
    }
}
