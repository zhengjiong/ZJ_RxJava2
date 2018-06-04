package com.example.operate;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * CreateTime:18/6/4  10:39
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example13Debounce {

    /**
     * 去除发送频率过快的项
     */
    public static void main(String[] args) {
        test1();
    }

    /**
     * 去除发送间隔时间小于 500 毫秒的发射事件，所以 1 和 3 被去掉了。
     * <p>
     * 输出:
     * onNext 2
     * onNext 4
     * onNext 5
     * onComplete
     */
    private static void test1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1); // skip
                Thread.sleep(400);
                emitter.onNext(2); // send
                Thread.sleep(505);
                emitter.onNext(3); // skip
                Thread.sleep(100);
                emitter.onNext(4); // send
                Thread.sleep(605);
                emitter.onNext(5); // send
                Thread.sleep(510);
                emitter.onComplete();
            }
        }).debounce(500, TimeUnit.MILLISECONDS).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
