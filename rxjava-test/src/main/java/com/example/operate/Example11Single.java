package com.example.operate;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * CreateTime:18/6/4  10:28
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example11Single {

    /**
     * 顾名思义，Single 只会接收一个参数，而 SingleObserver 只会调用 onError() 或者 onSuccess()。
     *
     */
    public static void main(String[] args){
        test1();
    }

    /**
     * 输出:
     * onSuccess -> 1
     */
    private static void test1() {
        Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> e) throws Exception {
                e.onSuccess(1);
                e.onSuccess(2);
            }
        }).subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer integer) {
                System.out.println("onSuccess -> " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError " + e.getMessage());
            }
        });
    }
}
