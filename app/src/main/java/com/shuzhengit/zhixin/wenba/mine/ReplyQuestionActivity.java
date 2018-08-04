package com.shuzhengit.zhixin.wenba.mine;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.base.BaseActivity;
import com.library.bean.BaseCallModel;
import com.library.rx.RxBus2;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.EventType;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.bean.WenBa;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.RxSubscriber;
import com.shuzhengit.zhixin.util.CheckUser;
import com.shuzhengit.zhixin.util.EventCodeUtils;
import com.shuzhengit.zhixin.util.GlideLoadImageUtils;
import com.shuzhengit.zhixin.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ReplyQuestionActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvRight)
    TextView mTvRight;
    @BindView(R.id.ivRightShare)
    ImageView mIvRightShare;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.civQuestionAvatar)
    CircleImageView mCivQuestionAvatar;
    @BindView(R.id.tvQuestionUserName)
    TextView mTvQuestionUserName;
    @BindView(R.id.tvQuestionInfo)
    TextView mTvQuestionInfo;
    @BindView(R.id.etReplyInfo)
    EditText mEtReplyInfo;
    private Unbinder mUnbinder;

    @Override
    protected int layoutId() {
        return R.layout.activity_reply_question;
    }

    @Override
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        WenBa wenba = (WenBa) getIntent().getSerializableExtra("wenba");
        if (wenba == null) {
            failure("找不到相关的问题");
            onBackPressed();
            return;
        }
        mTvTitle.setText("回复问题");
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("回复");
        mTvQuestionUserName.setText(wenba.getNickName());
        mTvQuestionInfo.setText(wenba.getDescription());
        User user = CheckUser.checkUserIsExists();
        if (user==null){
            failure("请先登录");
            return;
        }
        if (TextUtils.isEmpty(wenba.getAvatarUrl())){
            mCivQuestionAvatar.setImageResource(R.drawable.ic_normal_icon);
        }else {
            GlideLoadImageUtils.loadImg(this,wenba.getAvatarUrl(),mCivQuestionAvatar);
        }
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mEtReplyInfo.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    failure("内容不能为空");
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("questionId", wenba.getId());
                    jsonObject.put("replayUserId",user.getId() );
                    jsonObject.put("content", s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .postReply(body)
                        .compose(RxSchedulersHelper.io_main())
                        .subscribeWith(new RxSubscriber<BaseCallModel>() {
                            @Override
                            protected void _onNext(BaseCallModel callModel) {
                                if (callModel.code == 200) {
                                    failure("回复成功");
                                    EventType<Integer> integerEventType = new EventType<>(EventCodeUtils
                                            .REFRESH_REPLYS, 1);
                                    RxBus2.getDefault().post(integerEventType);
                                    onBackPressed();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void createPresenter() {

    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
