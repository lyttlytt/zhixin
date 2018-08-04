package com.shuzhengit.zhixin.question.release_question;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.library.base.BaseFragment;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.bean.QuestionTag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/25 14:27
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:发布问题结果页面
 */

public class ReleaseQuestionAddTagFragment extends BaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.btnAddTag)
    Button mBtnAddTag;
    @BindView(R.id.etAddTag)
    EditText mEtAddTag;
    private ReleaseQuestionActivity mActivity;
    private Question mReleaseQuestion;
    private int mMemberId;
    private Unbinder mUnbinder;


    private TagAdapter mTagAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_question_tag;
    }

    public static ReleaseQuestionAddTagFragment getInstance(int memberId) {
        ReleaseQuestionAddTagFragment releaseQuestionAddTagFragment = new ReleaseQuestionAddTagFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("memberId", memberId);
        releaseQuestionAddTagFragment.setArguments(bundle);
        return releaseQuestionAddTagFragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);
        mActivity = (ReleaseQuestionActivity) mActivity;
        mMemberId = getArguments().getInt("memberId");
        GridLayoutManager layoutManager = new GridLayoutManager(getHoldingActivity(), 4);
        mRecyclerView.setLayoutManager(layoutManager);
        mTagAdapter = new TagAdapter(getHoldingActivity());
        mRecyclerView.setAdapter(mTagAdapter);
        mBtnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = mEtAddTag.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)){
                    mTagAdapter.addTag(trim);
                    mEtAddTag.setText("");
                }
            }
        });
    }

    @Override
    protected void createPresenter() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    public void setQuestionTags(List<QuestionTag> tags) {
        List<String> list = new ArrayList<>();
        for (QuestionTag tag : tags) {
            list.add(tag.getTitle());
        }
        mTagAdapter.setTags(list);
    }

    public List<String> getTags(){
        return mTagAdapter.getTags();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
