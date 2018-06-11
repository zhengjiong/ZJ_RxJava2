package com.example.operate;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiFunction;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * CreateTime:18/6/11  13:33
 *
 * @author 郑炯
 * @version 1.0
 */
public class Example22Zip {

    /**
     * zip:
     * 最终下游收到的事件数量 是和上游中发送事件最少的那一根水管的事件数量 相同.
     * 这个也很好理解, 因为是从每一根水管 里取一个事件来进行合并, 最少的 那个肯定就最先取完 ,
     * 这个时候其他的水管尽管还有事件 , 但是已经没有足够的事件来组合了, 因此下游就不会收到剩余的事件了.
     * <p>
     * <p>
     * 使用场景:
     * 比如一个界面需要展示用户的一些信息, 而这些信息分别要从两个服务器接口中获取, 而只有当两个都获取到了之后才能进行展示, 这个时候就可以用Zip了
     */
    public static void main(String[] args) {

        //test1();
        test2();
    }

    /**
     * 如果在同一个线程, observable2会等到observable1发送完毕才会开始发送
     * 输出:
     * emit 1
     * emit 2
     * emit 3
     * emit 4
     * emit complete1
     * emit A
     * onNext: 1A
     * emit B
     * onNext: 2B
     * emit C
     * onNext: 3C
     * emit complete2
     * onComplete
     */
    private static void test1() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("emit 1");
                emitter.onNext(1);
                System.out.println("emit 2");
                emitter.onNext(2);
                System.out.println("emit 3");
                emitter.onNext(3);
                System.out.println("emit 4");
                emitter.onNext(4);
                System.out.println("emit complete1");
                //这里不加onComplete也可以,onNext(4)之后, observable2就会开始发送
                emitter.onComplete();
            }
        });

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("emit A");
                emitter.onNext("A");
                System.out.println("emit B");
                emitter.onNext("B");
                System.out.println("emit C");
                emitter.onNext("C");
                System.out.println("emit complete2");
                emitter.onComplete();
            }
        });

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                System.out.println("onNext: " + s);
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
    }

    /**
     * observable1和observable2在不同的线程, 这样observable1就不会等到observable2发送完毕才开始发送,
     * 两者发交替发送
     * 输出:
     * emit 1
     * emit A
     * onNext: 1A
     * emit B
     * emit 2
     * onNext: 2B
     * emit 3
     * emit C
     * onNext: 3C
     * emit 4
     * emit complete2
     * onComplete
     */
    private static void test2() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("emit 1");
                emitter.onNext(1);

                Thread.sleep(1000);
                System.out.println("emit 2");
                emitter.onNext(2);

                Thread.sleep(1000);
                System.out.println("emit 3");
                emitter.onNext(3);

                Thread.sleep(1000);
                System.out.println("emit 4");
                emitter.onNext(4);

                Thread.sleep(1000);
                System.out.println("emit complete1");
                //这里不加onComplete也可以,onNext(4)之后, observable2就会开始发送
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("emit A");
                emitter.onNext("A");

                Thread.sleep(1000);
                System.out.println("emit B");
                emitter.onNext("B");

                Thread.sleep(1000);
                System.out.println("emit C");
                emitter.onNext("C");

                Thread.sleep(1000);
                System.out.println("emit complete2");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                System.out.println("onNext: " + s);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
                try {
                    System.in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
