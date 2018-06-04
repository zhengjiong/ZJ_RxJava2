package com.example.operate;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

/**
 * CreateTime:18/6/4  13:53
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example16Reduce {

    /**
     * reduce, 把所有的操作都操作完成之后在调用一次观察者，把数据一次性输出
     *
     * 输出:
     * i1=9 ,i2=1
     * i1=10 ,i2=2
     * i1=12 ,i2=3
     * onSuccess -> 15
     */
    public static void main(String[] args) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);

                //必须要执行onComplete, 下面的SingleObserver才能在onSuccess接收到最后的值
                e.onComplete();
            }
            //这里的seed, 可以不加, 加了就作为初始值
        }).reduce(9, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer i1, Integer i2) throws Exception {
                System.out.println("i1=" + i1 + " ,i2=" + i2);
                return i1 + i2;
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
