package com.example.backpressure;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * https://www.jianshu.com/p/9b1304435564
 *
 * Created by zj on 2017/4/4.
 */

public class Example3_Flowable_DROP {
    private static Subscription subscription;

    /**
     * 安卓测试效果看app项目中的com.zj.example.rxjava2.backpressure.FlowableExample
     * <p>
     * 输出:
     * onSubscribe
     * onNext 1
     * onNext 2
     * onNext 3
     * ...
     * onNext 869738413
     * onNext 869738414
     * ...
     */
    public static void main(String[] args) {

        /**
         * 这里必须使用Schedulers.newThread(),让事件源和订阅者运行在不同的线程上才不会造成线程堵塞而影响测试结果,
         * 最后还需要加上System.in.read();才会有打印输出
         *
         * 输出:
         */
        Flowable.create(new FlowableOnSubscribe<Long>() {
            @Override
            public void subscribe(final FlowableEmitter<Long> e) throws Exception {
                for (long i = 1; ; i++) {
                    //System.out.println("emmit " + i + " " + Thread.currentThread().toString());
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.DROP).subscribe(new Subscriber<Long>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                subscription = s;
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("onNext " + aLong +  " " + Thread.currentThread().toString());
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

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * Flowable内部的默认的水缸大小为128, 因此, 它刚开始肯定会把1-128这128个事件保存起来,
         * 然后丢弃掉其余的事件, 当我们request(128)的时候,下游便会处理掉这128个事件, 那么上游水缸中又会重新装进新的128个事件,
         * 因为上游一直在发送事件,所以第二次request的时候, 不会从128开始, 而是很大的一个数字
         */
        subscription.request(128);
        //因为默认水缸中最多只能存放128个(buffer除外),用129也只能最多请求128个
        //subscription.request(129);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        subscription.request(128);

        try {
            System.in.read();//必须要加上这一句才会有输出
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
