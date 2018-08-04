package com.shuzhengit.zhixin.question.answer;

import com.library.bean.BaseCallModel;
import com.library.rx.RxSchedulersHelper;
import com.shuzhengit.zhixin.bean.DocumentPicture;
import com.shuzhengit.zhixin.rx.RxSubscriber;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 09:17
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class AddAnswerPresenter extends AddAnswerContract.Presenter {

    private final AddAnswerModel mModel;

    public AddAnswerPresenter(AddAnswerContract.View view) {
        super(view);
        mModel = new AddAnswerModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void postAnswer(String answer, int questionId, int memberId, List<DocumentPicture> pictures) {
        mCompositeDisposable.add(mModel.postAnswer(answer, questionId, memberId, pictures)
                .compose(RxSchedulersHelper.io_main())
                .subscribeWith(new RxSubscriber<BaseCallModel>() {
                    @Override
                    protected void _onNext(BaseCallModel callModel) {
                        mView.refreshAnswerList();
                    }
                }));
    }
}
