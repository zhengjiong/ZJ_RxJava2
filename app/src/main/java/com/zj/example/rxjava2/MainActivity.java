package com.zj.example.rxjava2;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 举个简单的例子，在 RxJava1.x 中的 observeOn， 因为是切换了消费者的线程，因此内部实现用队列存储事件。
         * 在 Android 中默认的 buffersize 大小是16，因此当消费比生产慢时， 队列中的数目积累到超过16个，
         * 就会抛出MissingBackpressureException， 初学者很难明白为什么会这样，使得学习曲线异常得陡峭。

         * 而在2.0 中，Observable 不再支持背压，而Flowable 支持非阻塞式的背压。Flowable是RxJava2.0
         * 中专门用于应对背压（Backpressure）问题。所谓背压，即生产者的速度大于消费者的速度带来的问题，
         * 比如在Android中常见的点击事件，点击过快则经常会造成点击两次的效果。其中，Flowable默认队列大小为128。
         * 并且规范要求，所有的操作符强制支持背压。幸运的是， Flowable 中的操作符大多与旧有的 Observable 类似。
         */
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("1");
                e.onNext("2");
                e.onNext("3");
                e.onNext("4");
                e.onNext("5");
                e.onNext("6");
                e.onNext("7");
                e.onNext("8");
                e.onNext("9");
                e.onNext("10");
                e.onNext("11");
                e.onNext("12");
                e.onNext("13");
                e.onNext("14");
                e.onNext("15");
                e.onNext("16");
                e.onNext("17");
            }
        }, BackpressureStrategy.ERROR).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                SystemClock.sleep(3000);
                System.out.println("onNext " + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                System.out.println("onError " + throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("onComplete");
            }
        });
        /*Observable.create(
                new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        e.onNext("1");
                        e.onNext("2");
                        e.onNext("3");
                        e.onNext("4");
                        e.onNext("5");
                        e.onNext("6");
                        e.onNext("7");
                        e.onNext("8");
                        e.onNext("9");
                        e.onNext("10");
                        e.onNext("11");
                        e.onNext("12");
                        e.onNext("13");
                        e.onNext("14");
                        e.onNext("15");
                        e.onNext("16");
                        e.onNext("17");
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String String) throws Exception {
                        SystemClock.sleep(3000);
                        System.out.println("onNext " + String);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        System.out.println("onError " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("onComplete");
                    }
                });*/

    }
}
