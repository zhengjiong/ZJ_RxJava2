package com.example.error;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Flowable和Observable不同, 两种方式都可以捕获
 * CreateTime:18/7/3  09:25
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example11_Flowable_ErrorTest {

    public static void main(String[] args) {
        //test1();
        test2();
    }

    /**
     * Flowable和Observable不同, 两种方式都可以捕获
     * onNext 1
     * onError java.lang.RuntimeException: zhengjiong
     */
    public static void test1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) {
                e.onNext(1);
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
                //这里抛出异常后, onError可以捕获
                throw new RuntimeException("zhengjiong");
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError " + t);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    /**
     * Flowable和Observable不同, 两种方式都可以捕获
     * onNext 1
     * onError java.lang.RuntimeException: zhengjiong
     */
    public static void test2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) {
                e.onNext(1);
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println("onNext " + integer);
                //这里抛出异常后, onError可以捕获
                throw new RuntimeException("zhengjiong");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                System.out.println("onError " + throwable);
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("onComplete");
            }
        });
    }
}
