package com.shuzhengit.zhixin.mine;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.bean.BaseCallModel;
import com.library.rx.RxBus2;
import com.library.rx.RxResultHelper;
import com.library.rx.RxSchedulersHelper;
import com.library.util.LogUtils;
import com.library.util.SPUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.School;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.CacheKeyManager;
import com.shuzhengit.zhixin.util.CacheUtils;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.view.pull2refreshview.PullToRefreshRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SelectSchoolActivity extends BaseActivity {
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.searchView)
    SearchView mSearchView;
    @BindView(R.id.recyclerview)
    PullToRefreshRecyclerView mRecyclerview;
    private SchoolAdapter mSchoolAdapter;
    private ArrayList<School> mLocationSchools = new ArrayList<>();
    private User mUser;
    private Unbinder mUnbinder;

    @Override
    protected int layoutId() {
        return R.layout.activity_select_school;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void createPresenter() {
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setIconified(false);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint("搜索你的学校");
//        ImageView searchBtn = (ImageView) mSearchView.findViewById(R.id.search_go_btn);
//        searchBtn.setImageResource(R.drawable.ic_no_send);
        mSearchView.clearFocus();
        mRecyclerview.setShowPullToRefresh(false);
        mRecyclerview.setShowLoadMore(false);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mSchoolAdapter = new SchoolAdapter();
        mRecyclerview.setAdapter(mSchoolAdapter);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                HttpProtocol.getApi()
                        .findSchoolByLike(query)
                        .compose(RxSchedulersHelper.io_main())
                        .compose(RxResultHelper.handleResult())
                        .subscribe(new RxSubscriber<List<School>>(SelectSchoolActivity.this) {
                            @Override
                            protected void _onNext(List<School> schools) {
                                mSchoolAdapter.searchInfo(schools);
                            }
                        });
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                LogUtils.i(newText.length() + "");
                if (newText.length() == 0) {
                    mSchoolAdapter.showLocationSchool();
                }
                return false;
            }
        });
        String cityCode = (String) SPUtils.get(this, CacheKeyManager.BDCityCode, "");
        if (!TextUtils.isEmpty(cityCode)) {
            fetchLocationSchool(cityCode);
        }
        mSchoolAdapter.setOnItemSchoolClickListener(new SchoolAdapter.OnItemSchoolClickListener() {
            @Override
            public void onItemSchoolClick(School school) {
                mUser = CheckUser.checkUserIsExists();
                if (mUser!=null) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("schoolId", school.getId());
                        jsonObject.put("modifyType", "school");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            jsonObject.toString());
                    HttpProtocol
                            .getApi()
                            .modifyUserInfo(String.valueOf(mUser.getId()), requestBody)
                            .compose(RxSchedulersHelper.io_main())
                            .subscribe(new RxSubscriber<BaseCallModel<Object>>(SelectSchoolActivity.this) {
                                @Override
                                protected void _onNext(BaseCallModel<Object> baseCallModel) {
                                    failure(baseCallModel.getMessage());
                                    int schoolId = (int) baseCallModel.getData();
                                    mUser.setSchoolId(schoolId);
                                    mUser.setSchoolName(school.getName());
                                    CacheUtils.getCacheManager(APP.getInstance()).remove(CacheKeyManager.USER);
                                    CacheUtils.getCacheManager(APP.getInstance()).put(CacheKeyManager.USER, mUser);
                                    RxBus2.getDefault().post(new EventType<Integer>(EventCodeUtils.USER_SCHOOL_ID, school.getId()));
//                                    SPUtils.put(APP.getInstance(),CacheKeyManager.MODIFY_SCHOOL_ID,true);
                                    onBackPressed();
                                }
                            });
                }else {
                    SPUtils.put(APP.getInstance(),CacheKeyManager.ANONYMOUS_SCHOOL_ID, school.getId());
                    RxBus2.getDefault().post(new EventType<Integer>(EventCodeUtils.USER_SCHOOL_ID, school.getId()));
                    onBackPressed();
                }
            }
        });
//        mSearchView.setImeOptions(3);
    }

    private void fetchLocationSchool(String cityCode) {
        HttpProtocol.getApi()
                .findSchoolByBaiduCityCode(cityCode)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxResultHelper.handleResult())
                .subscribe(new RxSubscriber<List<School>>(this) {
                    @Override
                    protected void _onNext(List<School> schools) {
                        mSchoolAdapter.setLocationSchools(schools);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
