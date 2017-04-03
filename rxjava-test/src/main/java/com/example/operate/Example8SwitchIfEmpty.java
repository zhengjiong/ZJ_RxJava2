package com.example.operate;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Title: Example7SwitchIfEmpty
 * Description:
 * Copyright:Copyright(c)2016

 * CreateTime:17/3/31  17:22
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example8SwitchIfEmpty {
    static BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();
    public static void main(String[] args){

        /*Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

            }
        })*/
        behaviorSubject.switchMap(new Function<Integer, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(@NonNull Integer integer) throws Exception {
                System.out.println("switchMap " + integer);
                //return Observable.just(integer);
                return Observable.empty();
            }
        })
        .switchIfEmpty(new Observable<Integer>() {
            @Override
            protected void subscribeActual(Observer<? super Integer> observer) {
                System.out.println("switchIfEmpty");
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

        //behaviorSubject.onNext(1);
        //behaviorSubject.onNext(2);
    }
}
