package com.example.error;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Cancellable;

/**
 * Created by zj on 2017/3/30.
 */

public class Example3_Flowable {

    public static void main(String[] args) {
        //test1();
        test2();
    }

    private static void test1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                e.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        System.out.println("cancel");
                    }
                });
                e.onNext(1);
                e.onNext(2);

                System.out.println("new RuntimeException(\"error1\")");
                e.onError(new RuntimeException("error1"));
                System.out.println("throw error2");
                /**
                 * 注意:
                 * 多吃onError会导致程序崩溃!,
                 * 判断是否已经解除订阅, 解除订阅后讲不会再发送error
                 */

                if (!e.isCancelled()) {
                    e.onError(new RuntimeException("error2"));
                    throw new RuntimeException("error3");
                }
            }
        }, BackpressureStrategy.LATEST).subscribe(new Subscriber<Integer>() {
            Subscription subscription;
            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError " + t.getMessage());
                /**
                 * 这里执行subscription.cancel(), 没有任何意义, 因为onError的时候,
                 * 就已经取消订阅了
                 */
                subscription.cancel();
            }

            @Override
            public void onComplete() {

            }
        });
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        System.out.println("dispose");
                    }

                    @Override
                    public boolean isDisposed() {
                        return false;
                    }
                });
                e.onNext(1);
                e.onNext(2);

                System.out.println("new RuntimeException(\"error1\")");
                e.onError(new RuntimeException("error1"));
                System.out.println("throw error2");
                /**
                 * 注意:
                 * 多吃onError会导致程序崩溃!,
                 * 判断是否已经解除订阅, 解除订阅后讲不会再发送error
                 */

                if (!e.isDisposed()) {
                    e.onError(new RuntimeException("error2"));
                    throw new RuntimeException("error3");
                }
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("doOnDispose");
            }
        }).subscribe(new Observer<Integer>() {
            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError " + e.getMessage());
                /**
                 * 这里执行disposable.dispose(), 没有任何意义, 因为onError的时候,
                 * 就已经取消订阅了
                 */
                disposable.dispose();
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    private static void test2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                e.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        System.out.println("cancel");
                    }
                });
                e.onNext(1);
                e.onNext(2);

                System.out.println("throw error1");
                System.out.println(1 / 0);//这里遇到错误后,不会再继续执行下面的代码,所以不会导致第二次onError,所以程序不会崩溃
                e.onError(new RuntimeException("error1"));
                System.out.println("throw error2");

                /**
                 * 注意:
                 * 多吃onError会导致程序崩溃!,
                 * 判断是否已经解除订阅, 解除订阅后不再发送error
                 */
                if (!e.isCancelled()) {
                    e.onError(new RuntimeException("error2"));
                    throw new RuntimeException("error3");
                }

            }
        }, BackpressureStrategy.LATEST).subscribe(new Subscriber<Integer>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext " + integer);
            }

            @Override
            public void onError(Throwable t) {
                /**
                 * 这里执行subscription.cancel(), 没有任何意义, 因为onError的时候,
                 * 就已经取消订阅了
                 */
                subscription.cancel();
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
