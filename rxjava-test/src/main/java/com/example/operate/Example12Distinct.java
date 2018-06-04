package com.example.operate;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * CreateTime:18/6/4  10:31
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example12Distinct {

    /**
     * distinct, 去重操作符，简单的作用就是去重。
     */
    public static void main(String[] args) {
        test1();
    }

    /**
     输出:
     onNext -> 1
     onNext -> 3
     onComplete


     */
    private static void test1() {
        Observable.just(1, 1, 3).distinct().subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext -> " + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
