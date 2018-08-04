package com.library.rx;


import com.library.bean.BaseCallModel;
import com.library.exception.ServerException;
import com.library.util.LogUtils;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Function;


/**
 * Author：where on 2017/3/2 17:26
 * 处理服务器返回的东西
 */

public class RxResultHelper {
    private static final String TAG = "RxResultHelper";
    public static <T> FlowableTransformer<BaseCallModel<T>, T> handleResult() {
        return new FlowableTransformer<BaseCallModel<T>, T>() {
            @Override
            public Publisher<T> apply(Flowable<BaseCallModel<T>> upstream) {
                return upstream.flatMap(new Function<BaseCallModel<T>,
                        Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(BaseCallModel<T> tBaseCallModel) throws
                            Exception {
                        LogUtils.e("TAG",tBaseCallModel.toString());
                        if (tBaseCallModel.code == BaseCallModel.SUCCESS && tBaseCallModel.isOk()) {
                            return createData(tBaseCallModel.getData());
                        } else {
                            if (tBaseCallModel.code == 204) {
                                LogUtils.d(TAG,"204 ");
                                return Flowable.error(new ServerException("没有收藏的文章"));
//                                return getTObservable(204, "没有收藏的文章");
                            } else if (tBaseCallModel.code == 400) {
                                LogUtils.d(TAG,"400 ");
                                return createData(tBaseCallModel.getData());
                            } else if (tBaseCallModel.code == 404) {
                                LogUtils.d(TAG,"404 ");
                                return getTObservable(404, "没有找到对应的资源,请稍后重试");
                            } else if (tBaseCallModel.code == 500) {
                                LogUtils.d(TAG,"500 ");
                                return getTObservable(500, "服务器异常,请稍后重试");
                            } else {
                                LogUtils.d(TAG,tBaseCallModel.getCode()+"");
                                return getTObservable(tBaseCallModel.getCode(), "服务器异常,请稍后重试");
                            }
                        }
//                        if (!tBaseCallModel.isError())
//                        if (tBaseCallModel.getServerCode()==tBaseCallModel
//                                .getCode())
//                       return createData(tBaseCallModel.result);
                        // FIXME: 2017/6/22 根据后台数据再次封装请求回来后的数据

//                            return null;
//                        return createData(tBaseCallModel.result<T>);
//                        else
//                            return getTObservable(404,"page not found ");
                    }
                });

//            @Override
//            public Observable<T> call(Observable<BaseCallModel<T>>
// tObservable) {
//                return tObservable.flatMap(new Func1<BaseCallModel<T>,
// Observable<T>>() {
//                    @Override
//                    public Observable<T> call(BaseCallModel<T> result) {
//                        // TODO: 2017/6/9 和服务端对接 请求成功后的code
//                        if (result.getServerCode()==BaseCallModel.SUCCESS){
//                            return createData(result.t);
//                        }else {
//                            // TODO: 2017/6/9 失败的话 返回失败的信息 和serverCode
//                            return getTObservable(result.getServerCode(),
// result.getServerMessage());
//                        }
//                    }
//                });
//                return tObservable.flatMap(new Func1<T, Observable<T>>() {
//                    @Override
//                    public Observable<T> call(T t) {
//                        if (t.getCode()==BaseCallModel.SUCCESS){
//                            return createData(t);
//                        }else{
//                            return getTObservable(t.getCode(),t.getError());
//                        }
//                    }
//                });
            }

            /**
             * 错误信息返回的方法
             * @param code
             * @param error
             * @return
             */
            private Flowable<T> getTObservable(final int code, final String
                    error) {
                return Flowable.create(e -> {
                    try {
//                        new ServerException(code + File.separator + error);
                         new ServerException(error);
//                        e.onError(serverException);
//                        new ServerException(error);
                    } catch (Exception ex) {
//                        e.onError(ex);
                        LogUtils.i(TAG,ex.getMessage());
                        e.onError(ex);

                    }
                }, BackpressureStrategy.BUFFER);
//                return Observable.create(new Observable.OnSubscribe<T>() {
//                    @Override
//                    public void call(Subscriber<? super T> subscriber) {
//                        try {
//                            new ServerException(code + File.separator + error);
//                        } catch (Exception e) {
//                            subscriber.onError(e);
//                        }
//                    }
//                });
            }
        }

                ;
    }


    /**
     * 请求成功，返回数据
     *
     * @param <T>
     * @param t
     * @return
     */
    private static <T> Flowable<T> createData(final T t) {
//        return Flowable.create(new Observable.OnSubscribe<T>() {
//            @Override
//            public void call(Subscriber<? super T> subscriber) {
//                try {
//                    subscriber.onNext(t);
//                    subscriber.onCompleted();
//                } catch (Exception e) {
//                    subscriber.onError(e);
//                }
//            }
//        });

        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> e) throws Exception {
                LogUtils.d(TAG,t.toString());
                try {
                    e.onNext(t);
                    e.onComplete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    e.onError(ex);
                }

            }
        }, BackpressureStrategy.BUFFER);
    }
}
