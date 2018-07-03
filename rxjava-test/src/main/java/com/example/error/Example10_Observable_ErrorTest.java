package com.example.error;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 异常捕获的坑!
 * CreateTime:18/6/22  13:57
 *
 * @author 郑炯
 * @version 1.0
 */
public class Examle10_ErrorTest {

    public static void main(String[] args){
        //test1();
        test2();

    }

    /**
     * 如果subscribe的时候传入的参数是Observer, 那么在onNext中如果抛出异常,
     * onError就会不能捕获, 程序直接报错.
     */
    private static void test1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
                //抛出异常后, onError不能捕获
                throw new NullPointerException("zhengjiong");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError -> " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void test2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("onNext " + integer);
                //抛出异常onError可以捕获
                throw new NullPointerException("zhengjiong");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("onError" + throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("onComplete");
            }
        });

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
