package com.example.error;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by zj on 2017/3/30.
 */

public class Example5 {

    /**
     * 结果:
     * Observable.just 1
     * onNext 1
     * flatMap Observable.error error-1
     * dispose
     * onError java.lang.RuntimeException: error-1
     * e.onNext 3
     * e.onNext 4
     */
    public static void main(String[] args) {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        System.out.println("dispose");
                    }

                    @Override
                    public boolean isDisposed() {
                        return false;
                    }
                });
                /**
                 * 当flatMap中error之后, 这里还是会继续执行, 但是error之后并不会进入flatMap中,
                 * 所以也不会导致两次error, 所以程序不会崩溃
                 */
                e.onNext(1);
                e.onNext(2);
                System.out.println("e.onNext 3");
                e.onNext(3);
                System.out.println("e.onNext 4");
                e.onNext(4);
                e.onComplete();
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                if (integer >= 2) {
                    System.out.println("flatMap Observable.error error-1");
                    return Observable.error(new RuntimeException("error-1"));
                }
                System.out.println("Observable.just " + integer);
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
