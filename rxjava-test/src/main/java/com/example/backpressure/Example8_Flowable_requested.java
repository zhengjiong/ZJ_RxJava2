package com.example.backpressure;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * 这个例子是读取一个文本文件，
 * 需要一行一行读取，
 * 然后处理并输出，
 * 如果文本文件很大的时候，
 * 比如几十M的时候，
 * 全部先读入内存肯定不是明智的做法，
 * 因此我们可以一边读取一边处理
 *
 *
 * CreateTime:18/7/9  11:02
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example8_Flowable_requested {
    static Subscription subscription;

    /**
     * 输出:
     * onNext 这个例子是读取一个文本文件，
     * onNext 需要一行一行读取，
     * onNext 然后处理并输出，
     * onNext 如果文本文件很大的时候，
     * onNext 比如几十M的时候，
     * onNext 全部先读入内存肯定不是明智的做法，
     * onNext 因此我们可以一边读取一边处理
     * onComplete
     */
    public static void main(String[] args) {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                FileReader fileReader = new FileReader(new File("/Users/zhengjiong/android/workspace_android_studio/ZJ_RxJava2/rxjava-test/src/main/resources/text.txt"));
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String str;

                //e.isCancelled()可以不需要加, 主要是方式subscription.cancel的情况
                while ((str = bufferedReader.readLine()) != null && !e.isCancelled()) {
                    while (e.requested() == 0) {
                        //暂停发送
                    }
                    e.onNext(str);
                }

                bufferedReader.close();
                fileReader.close();
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                s.request(1);
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext " + s);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscription.request(1);

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
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
