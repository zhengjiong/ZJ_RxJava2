package com.example.scene;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.NoSuchElementException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Title: Example2Concat
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/3/29  17:56
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example2Concat {

    /**
     * 输出:
     * filter cache-abc
     * onErrorResumeNext
     * onComplete
     */
    public static void main(String[] args) {
        Flowable.concat(getCache("abc"), getNetwork("empty"))
                .switchIfEmpty(new Flowable<String>() {
                    @Override
                    protected void subscribeActual(Subscriber<? super String> s) {
                        /**
                         * getCache和getNetwork都返回empty(),才会进入switchIfEmpty,
                         * 光getNetwork返回Empty不会进入
                         */
                        System.out.println("switchIfEmpty");
                        //s.onError(new NoSuchElementException("NoSuchElementException"));
                        //s.onNext("zhengjiong");
                    }
                })
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        System.out.println("filter " + s + " " + !"cache-abc".equals(s));
                        return !"cache-abc".equals(s);
                    }
                })
                //.firstElement()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
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
    }

    public static Flowable<String> getCache(final String type) {
        if ("empty".equals(type)) {
            return Flowable.empty();
        } else {
            return Flowable.create(new FlowableOnSubscribe<String>() {
                @Override
                public void subscribe(FlowableEmitter<String> e) throws Exception {
                    e.onNext("cache-" + type);
                    //e.onComplete();
                }
            }, BackpressureStrategy.BUFFER);
        }
    }

    public static Flowable<String> getNetwork(final String type) {
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                if ("empty".equals(type)) {
                    e.onError(new NullPointerException("NullPointerException"));
                    //throw new NullPointerException("NullPointerException");
                } else {
                    System.out.println("getNetwork-------------------");
                    e.onNext("network-" + type);
                    e.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER).doOnNext(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                /**
                 * doOnNext, 数据获取成功后缓存到本地
                 */
                System.out.println("doOnNext " + s + "保存在本地");
            }
        }).onErrorResumeNext(new Function<Throwable, Publisher<? extends String>>() {
            @Override
            public Publisher<? extends String> apply(@NonNull Throwable throwable) throws Exception {
                //出错拦截，当出现错误时，返回一个新的源而不是调用onError
                System.out.println("onErrorResumeNext");
                return Flowable.empty();
            }
        });
    }
}
