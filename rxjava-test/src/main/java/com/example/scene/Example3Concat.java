package com.example.scene;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Title: Example3Concat
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/3/30  09:08
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example3Concat {

    /**
     * 场景伪代码如下:
     *
     * 如果 (存在缓存 且 缓存未过期) {
     *   读取缓存并显示
     *   return
     * }
     * 请求网络
     * 更新缓存
     * 显示最新数据
     */
    public static void main(String[] args){
        //Flowable.concat(getCache("abc"), getNetwork("empty"))
        Flowable.concat(getCache("expire"), getNetwork("empty"))
                /**
                 * 最多发射一个数据，如果没有数据,则走onError。
                 *
                 * 读取到缓存后不会执行getNetwork中的Publisher.
                 */
                .firstOrError()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        System.out.println("onNext accept -> " + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        System.out.println("onError accept -> " + throwable.toString());
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
                    e.onComplete();
                }
            }, BackpressureStrategy.BUFFER).filter(new Predicate<String>() {
                @Override
                public boolean test(@NonNull String s) throws Exception {
                    /**
                     * 过滤掉过期的数据
                     */
                    if ("cache-expire".equals(s)) {
                        return false;
                    } else {
                        return true;
                    }
                }
            });
        }
    }

    public static Flowable<String> getNetwork(final String type) {
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                System.out.println("getNetwork start");
                if ("empty".equals(type)) {
                    e.onError(new NullPointerException("NullPointerException"));
                    //throw new NullPointerException("NullPointerException");
                } else {
                    e.onNext("network-" + type);
                    e.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER).doOnNext(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                /**
                 * doOnNext, 数据获取成功后更新缓存
                 */
                System.out.println("doOnNext " + s + " -> 更新缓存");
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
