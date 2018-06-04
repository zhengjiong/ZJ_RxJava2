package com.example.operate;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * CreateTime:18/6/4  14:14
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example18Interval {

    public static void main(String[] args) {
        /**
         * 创建一个无限的计时序列，每隔1秒发射一个数字，从 0 开始
         * 输出:
         onNext 0
         onNext 1
         onNext 2
         onNext 3
         onComplete
         */
        Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .take(4)//最多接收4个, 不然会无限执行下去
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        System.out.println("onNext " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });

        try {
            //必须要加上这一句才会有输出
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
