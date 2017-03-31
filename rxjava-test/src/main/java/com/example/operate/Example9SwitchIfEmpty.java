package com.example.operate;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.NoSuchElementException;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Title: Example7SwitchIfEmpty
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/3/31  17:22
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example9SwitchIfEmpty {
    static BehaviorProcessor<Integer> behaviorSubject = BehaviorProcessor.create();

    public static void main(String[] args) {

        /*Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

            }
        })*/
        behaviorSubject.switchMap(new Function<Integer, Publisher<Integer>>() {
            @Override
            public Publisher<Integer> apply(@NonNull Integer integer) throws Exception {
                System.out.println("switchMap " + integer);
                //return Observable.just(integer);
                return FlowableProcessor.empty();
            }
        }).switchIfEmpty(new Flowable<Integer>() {
            @Override
            protected void subscribeActual(Subscriber<? super Integer> s) {
                System.out.println("switchIfEmpty " + s);
                s.onError(new NoSuchElementException("NoSuchElementException"));
            }
        }).subscribe(new Consumer<Integer>() {
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
                System.out.println("onComplete");
            }
        });

        behaviorSubject.onNext(1);
        behaviorSubject.onNext(2);
    }
}
