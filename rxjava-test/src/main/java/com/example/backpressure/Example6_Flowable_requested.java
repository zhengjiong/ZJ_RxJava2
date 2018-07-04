package com.example.backpressure;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * 当上下游在同一个线程中的时候，在下游调用request(n)就会直接改变上游中的requested的值，多次调用便会叠加这个值，
 * 而上游每发送一个事件之后便会去减少这个值，当这个值减少至0的时候，继续发送事件便会抛异常了。
 * <p>
 * <p>
 * CreateTime:18/7/4  17:28
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example6_Flowable_requested {

    public static void main(String[] args) {
        //test1();
        //test2();
        test3();
    }

    /**
     * 下游没有调用request()，说明当前下游的处理能力为0，那么上游得到的requested也应该是0
     * 输出:
     * requested 0
     */
    private static void test1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) {
                System.out.println("requested " + e.requested());
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                //屏蔽掉
                //s.request(123);
            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 输出:
     * requested 10
     */
    private static void test2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) {
                System.out.println("requested " + e.requested());
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(10);
            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    /**
     * 当上下游在同一个线程中的时候，在下游调用request(n)就会直接改变上游中的requested的值，多次调用便会叠加这个值，
     * 而上游每发送一个事件之后便会去减少这个值，当这个值减少至0的时候，继续发送事件便会抛异常了。
     *
     * 输出:
     * onSubscribe
     * onNext 1
     * request amount 0
     * onError io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests
     * request amount 0
     */
    private static void test3() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) {
                e.onNext(1);
                System.out.println("request amount " + e.requested());
                e.onNext(2);
                System.out.println("request amount " + e.requested());
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                s.request(1);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError " + t);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
