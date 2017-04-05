package com.example.error;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.subscribers.SafeSubscriber;

/**
 * Created by zj on 2017/4/4.
 */

public class Example7_SafeSubscribe_Flowable {

    public static void main(String[] args) {
        //test1();
        test2();
    }

    /**
     * 解决在onNext中抛出异常的两种方式:
     * 方式1:看test2()
     * 方式2:看Example8
     */
    private static void test1() {
        Flowable.just(1, 2)
                .subscribe(new Subscriber<Integer>() {
                               @Override
                               public void onSubscribe(Subscription s) {
                                   s.request(Long.MAX_VALUE);
                               }

                               @Override
                               public void onNext(Integer t) {
                                   if (t == 1) {
                                       /**
                                        * 这里抛出异常会报错, Flowable不允许在onNext中抛出异常
                                        * 就算是System.out.println(1/0)这样也不行,
                                        * 如果要这样操作的话可以使用SafeSubscriber,
                                        * 看test2方法
                                        */
                                       //System.out.println(1/0);
                                       throw new IllegalArgumentException();

                                   }
                               }

                               @Override
                               public void onError(Throwable t) {
                                   System.out.println("onError " + t.toString());
                                   /*if (e instanceof IllegalArgumentException) {
                                       throw new UnsupportedOperationException();
                                   }*/
                               }

                               @Override
                               public void onComplete() {
                                   System.out.println("onComplete");
                               }
                           }
                );
    }

    /**
     * 解决在onNext中抛出异常的两种方式:
     * 方式1:看test2()
     * 方式2:看Example8
     */
    private static void test2() {
        Flowable.just(1, 2)
                //使用SafeSubscriber才可以在onNext中抛出异常
                .subscribe(new SafeSubscriber<Integer>(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer t) {
                        if (t == 1) {
                            throw new IllegalArgumentException();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + e.toString());
                       /*if (e instanceof IllegalArgumentException) {
                           throw new UnsupportedOperationException();
                       }*/
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                }));
    }
}
