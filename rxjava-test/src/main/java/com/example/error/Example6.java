package com.example.error;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by zj on 2017/3/30.
 */

public class Example6 {

    /**
     * 程序会崩溃
     */
    public static void main(String[] args) {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                e.onNext(1);

                /**
                 * 第一个onError会进入当观察者的onError中,
                 * 继续发送第二个onError的时候程序崩溃
                 */
                System.out.println("e.onError 1");
                e.onError(new RuntimeException("e.onError 1"));

                System.out.println("e.onError 4");
                e.onError(new RuntimeException("e.onError 4"));

                e.onComplete();
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                System.out.println("flatMap Observable.just " + integer);
                return Observable.just(integer.toString());
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                System.out.println("onNext " + s);
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
    }
}
