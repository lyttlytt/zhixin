package com.shuzhengit.zhixin.question.answer;

import com.library.base.BasePresenter;
import com.library.base.BaseView;
import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DocumentPicture;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 09:12
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class AddAnswerContract {
    interface View extends BaseView<AddAnswerContract.Presenter> {
        void refreshAnswerList();
    }

    public static abstract class Presenter extends BasePresenter<AddAnswerContract.View> {
        public Presenter(View view) {
            super(view);
            mView.setPresenter(this);
        }

        public abstract void postAnswer(String answer, int questionId, int memberId, List<DocumentPicture> pictures);
    }

    interface Model {
        Flowable<BaseCallModel> postAnswer(String answer, int questionId, int memberId, List<DocumentPicture> pictures);
    }
}
