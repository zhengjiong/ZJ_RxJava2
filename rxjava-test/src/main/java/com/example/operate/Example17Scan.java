package com.example.operate;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

/**
 * CreateTime:18/6/4  14:05
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example17Scan {

    /**
     * reduce, 把所有的操作都操作完成之后在调用一次观察者，把数据一次性输出.
     * scan 操作符作用和上面的 reduce 一致，唯一区别是 reduce 是个只追求结果的坏人，而 scan 会始终如一地把每一个步骤都输出。
     * <p>
     * 输出:
     * onNext -> 9
     * i1=9 ,i2=1
     * onNext -> 10
     * i1=10 ,i2=2
     * onNext -> 12
     * i1=12 ,i2=3
     * onNext -> 15
     * oncomplete
     */
    public static void main(String[] args) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);

                e.onComplete();//可以不加, Observer就不会收到onComplete
            }
            //这里的initialValue, 可以不加, 加了就作为初始值
        }).scan(9, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer i1, Integer i2) throws Exception {
                System.out.println("i1=" + i1 + " ,i2=" + i2);
                return i1 + i2;
            }
        }).subscribe(new Observer<Integer>() {
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
                System.out.println("oncomplete");
            }
        });
    }
}
