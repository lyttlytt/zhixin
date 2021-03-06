//package com.shuzhengit.zhixin.question.category_question;
//
//import com.library.base.BasePresenter;
//import com.library.base.BaseView;
//import com.library.bean.BaseCallModel;
//import com.shuzhengit.zhixin.bean.DataContainer;
//import com.shuzhengit.zhixin.bean.Question;
//
//import io.reactivex.Flowable;
//
///**
// * Created by 江苏镇江树正科技 .
// * Author：袁从斌 on 2017/9/26 15:48
// * E-mail:yuancongbin@gmail.com
// * <p>
// * 功能描述:
// */
//
//public class CategoryQuestionContract {
//    interface View extends BaseView<CategoryQuestionContract.Presenter>{
//        void refreshQuestions(DataContainer<Question> dataContainer);
//        void loadMoreQuestions(DataContainer<Question> dataContainer);
//        void refreshCompleted();
//        void loadMoreCompleted();
//        void loadMoreFail();
//        void loadNoMore();
//        void refreshQuestions();
//    }
//
//    public static abstract class Presenter extends BasePresenter<CategoryQuestionContract.View>{
//        public Presenter(View view) {
//            super(view);
//            mView.setPresenter(this);
//        }
//        abstract void findQuestions(int page,int pageNum,int categoryId);
//        abstract void refreshQuestion(int categoryId);
//    }
//    interface Model {
//        Flowable<BaseCallModel<DataContainer<Question>>> fetchQuestions(int page,int pageNum,int categoryId);
//    }
//}
