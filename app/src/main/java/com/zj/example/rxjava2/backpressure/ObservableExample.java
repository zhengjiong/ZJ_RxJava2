package com.zj.example.rxjava2.backpressure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zj on 2017/4/4.
 */

public class ObservableExample extends AppCompatActivity {
    private static Subscription subscription;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 如果在RxJava1中，内存数据超过128个时将会抛出MissingBackpressureException错误；
         * 而在RxJava2中并不会报错，数据会一直放到内存中，直到发生OutOfMemoryError。如下:
         *
         *  Caused by: java.lang.OutOfMemoryError: Failed to allocate a 16 byte allocation with 4182728 free bytes and 17MB until OOM; failed due to fragmentation (required continguous free 4096 bytes for a new buffer where largest contiguous free 0 bytes)
         *  04-04 10:43:24.479 11009-11009/com.zj.example.rxjava2 W/System.err:     at com.zj.example.rxjava2.LifecyclerRootActivity$5.accept(LifecyclerRootActivity.java:82)
         *
         */
       /* Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                for (long i = 0; i < Long.MAX_VALUE; i++) {
                    e.onNext(i);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                Thread.sleep(100);
                System.out.println("onNext " + aLong + " ," + Thread.currentThread().getName());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                System.out.println("onError " + throwable.toString());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("oncomplete");
            }
        });*/

        test3();
    }

    /**
     *
     * 因为一开始的订阅的时候就request了, 所以直接输出1-128, 然后这个时候上游还在不停的发出信息, 所以水缸会立马填充,
     * 但是并不是从128之后开始填充的,因为上游和下游处理是异步的,上游还在不停的发送,下游还没有开始处理的时候也一直在发送;
     * 然后再次request的时候会输出后面的, 最后一次request因为上游只发送10000个,后面的事件直接被抛弃了,所以没有任何输出!
     * 输出:
     * onSubscribe
     * onNext 1 Thread[RxNewThreadScheduler-2,5,main]
     * ......
     * onNext 128 Thread[RxNewThreadScheduler-2,5,main]
     * ......
     * onNext 4079 Thread[RxNewThreadScheduler-2,5,main]
     * onNext 4080 Thread[RxNewThreadScheduler-2,5,main]
     */
    private static void test3() {
        /**
         * 这里必须使用Schedulers.newThread(),让事件源和订阅者运行在不同的线程上才不会造成线程堵塞而影响测试结果,
         * 最后还需要加上System.in.read();才会有打印输出
         *
         * 输出:
         */
        Flowable.create(new FlowableOnSubscribe<Long>() {
            @Override
            public void subscribe(final FlowableEmitter<Long> e) throws Exception {
                for (long i = 1; i < 10000; i++) {
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Long>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                subscription = s;
                //一开始就处理掉128个事件
                s.request(128);
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("onNext " + aLong + " " + Thread.currentThread().toString());
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
         * Flowable内部的默认的水缸大小为128, 因此使用drop后,它刚开始肯定会把1-128这128个事件保存起来,
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
    }
}
