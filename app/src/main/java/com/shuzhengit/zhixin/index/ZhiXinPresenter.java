package com.shuzhengit.zhixin.index;

import android.support.v4.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.library.bean.BaseCallModel;
import com.library.rx.RxBus2;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.util.LogUtils;
import com.shuzhengit.zhixin.bean.Column;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.index.document.DocumentFragment;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.EventCodeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/14 13:32
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

class ZhiXinPresenter extends ZhiXinContract.Presenter {
    private static final String TAG = "ZhiXinPresenter";
    private final ZhiXinModel mZhiXinModel;
    private List<Column> mColumns;
    private int i =0;
    private int mCount;

    ZhiXinPresenter(ZhiXinContract.View view) {
        super(view);
        mZhiXinModel = new ZhiXinModel();
    }


    @Override
    public void start() {
        mCompositeDisposable.add(RxBus2.getDefault().toFlowable(EventType.class)
                .compose(RxSchedulersHelper.io_main())
                .filter(new Predicate<EventType>() {
                    @Override
                    public boolean test(EventType eventType) throws Exception {
                        return eventType.getEventType().equals(EventCodeUtils.CLICK_COLUMN);
                    }
                }).map(new Function<EventType, Column>() {
                    @Override
                    public Column apply(EventType eventType) throws Exception {
                        return (Column) eventType.getEvent();
                    }
                })
                .subscribeWith(new DisposableSubscriber<Column>() {
                    @Override
                    public void onNext(Column column) {
                        int position = 0;
                        for (int i = 0; i < mColumns.size(); i++) {
                            Column c = mColumns.get(i);
                            if (c.getId() != null && c.getId().equals(column.getId())) {
                                position = i;
                                break;
                            }
                        }
                        mView.switchPage(position);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
        mCompositeDisposable.add(RxBus2.getDefault().toFlowable(EventType.class)
                .filter(new Predicate<EventType>() {
                    @Override
                    public boolean test(EventType eventType) throws Exception {
                        return EventCodeUtils.ISMODIFY_CLICK_COLUMN.equals(eventType.getEventType());
                    }
                })
                .subscribeWith(new DisposableSubscriber<EventType>() {
                    @Override
                    public void onNext(EventType eventType) {
                        User user = CheckUser.checkUserIsExists();
                        if (user != null) {
                            updateColumnsWithCurrentPage(user.getId(), (JSONArray) eventType.getEvent(), eventType
                                    .getCurrentItem());
                        }

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
        mCompositeDisposable.add(RxBus2.getDefault().toFlowable(EventType.class)
                .filter(new Predicate<EventType>() {
                    @Override
                    public boolean test(EventType eventType) throws Exception {
                        return eventType.getEventType().equals(EventCodeUtils.MODIFY_COLUMN);
                    }
                })
                .map(new Function<EventType, JSONArray>() {
                    @Override
                    public JSONArray apply(EventType eventType) throws Exception {
                        return (JSONArray) eventType.getEvent();
                    }
                })
                .subscribeWith(new DisposableSubscriber<JSONArray>() {
                    @Override
                    public void onNext(JSONArray objects) {
                        LogUtils.d(TAG, "toFlowable MODIFY_COLUMN ");
                        User user = CheckUser.checkUserIsExists();
                        if (user != null) {
                            updateColumns(user.getId(), objects);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void findColumns(int memberId) {
        mCompositeDisposable.add(mZhiXinModel.findColumns(memberId)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<List<Column>>() {
                    @Override
                    protected void _onNext(List<Column> columns) {
                        if (columns == null) {
                            reSubscriber(memberId);
                            findNormalColumn();
                            againFindColumn(memberId);
                            return;
                        }
                        List<Fragment> fragments = addLocalColumns(columns);
                        mView.findColumnSuccess(columns, fragments);
                    }

                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.failure(message);
                    }
                }));
    }

    private void findNormalColumn() {
        mCompositeDisposable.add(mZhiXinModel.findColumns(0)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .subscribeWith(new RxSubscriber<List<Column>>() {
            @Override
            protected void _onNext(List<Column> columns) {
                List<Fragment> fragments = addLocalColumns(columns);
                mView.findColumnSuccess(columns, fragments);
            }
            @Override
            protected void _onError(String message) {
                super._onError(message);
                mView.failure(message);
            }
        }));
    }

    private List<Fragment> addLocalColumns(List<Column> columns) {
        mColumns = columns;
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            fragments.add(DocumentFragment.getInstance(columns.get(i).getCode()));
        }
//        String bdCity = (String) SPUtils.get(APP.getInstance(), CacheKeyManager.BDCity, "");
//        if (!TextUtils.isEmpty(bdCity)) {
//            Column city = new Column();
//            city.setColumnTitle("本地");
//            columns.add(0, city);
//            fragments.add(0, LocalCityFragment.getInstance(bdCity));
//        }
        return fragments;
    }

    @Override
    void findColumnsWithCurrentPage(int memberId, int columnId) {
        mCompositeDisposable.add(mZhiXinModel.findColumns(memberId)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<List<Column>>() {
                    @Override
                    protected void _onNext(List<Column> columns) {
                        if (columns == null) {
                            // TODO: 2017/11/29 注册用户添加默认关注失败,需要重新关注.
                            reSubscriber(memberId);
                            findNormalColumn();
                            againFindColumn(memberId);
                            return;
                        }
                        List<Fragment> fragments = addLocalColumns(columns);
                        mView.findColumnSuccessWithCurrentPage(columns, fragments, columnId);
                    }



                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        mView.failure(message);
                    }
                }));
    }

    private void againFindColumn(int memberId) {
        mCompositeDisposable.add(Flowable.timer(1, TimeUnit.SECONDS)
                .subscribeWith(new RxSubscriber<Long>() {
                    @Override
                    protected void _onNext(Long aLong) {
                        mZhiXinModel.findColumns(memberId)
                                .compose(RxSchedulersHelper.io_main())
                                .compose(RxResultHelper.handleResult())
                                .subscribeWith(new RxSubscriber<List<Column>>() {
                                    @Override
                                    protected void _onNext(List<Column> columns) {
                                        if (columns!=null) {
                                            List<Fragment> fragments = addLocalColumns(columns);
                                            mView.findColumnSuccess(columns, fragments);
                                        }else {
                                            dispose();
                                        }
                                    }
                                    @Override
                                    protected void _onError(String message) {
                                        super._onError(message);
                                        mView.failure(message);
                                    }
                                });
                    }
                }));
    }

    @Override
    void updateColumns(int memberId, JSONArray updateColumns) {
        mCompositeDisposable.add(mZhiXinModel.updateColumns(memberId, updateColumns)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        if ("ok".equals(s)) {
                            findColumns(memberId);
                        }
                    }
                }));
    }

    @Override
    void updateColumnsWithCurrentPage(int memberId, JSONArray updateColumns, int columnId) {
        mCompositeDisposable.add(mZhiXinModel.updateColumns(memberId, updateColumns)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        if ("ok".equals(s)) {
                            findColumnsWithCurrentPage(memberId, columnId);
                        }
                    }
                }));
    }

    private void reSubscriber(int memberId) {
        mCompositeDisposable.add(mZhiXinModel.reSubscriberColumn(memberId)
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<BaseCallModel>() {
                    @Override
                    protected void _onNext(BaseCallModel callModel) {

                    }
                }));
    }
}
