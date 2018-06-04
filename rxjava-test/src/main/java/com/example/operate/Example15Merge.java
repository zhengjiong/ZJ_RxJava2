package com.example.operate;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * CreateTime:18/6/4  11:14
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example15Merge {

    /**
     * 在 Rx 操作符中，merge 的作用是把多个 Observable 结合起来，接受可变参数，也支持迭代器集合。
     * 注意它和 concat 的区别在于，不用等到 发射器 A 发送完所有的事件再进行发射器 B 的发送。
     */
    public static void main(String[] args) {
        test1();
    }

    /**
     * 输出:
     * onNext -> 1
     * onNext -> 2
     * onNext -> 3
     * onNext -> 4
     * onNext -> 5
     * onComplete
     */
    private static void test1() {
        Observable.merge(Observable.just(1, 2, 3), Observable.just(4, 5))
                .subscribe(new Observer<Integer>() {
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
