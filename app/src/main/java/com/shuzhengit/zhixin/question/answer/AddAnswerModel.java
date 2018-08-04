package com.shuzhengit.zhixin.question.answer;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.DocumentPicture;
import com.shuzhengit.zhixin.http.HttpProtocol;
import com.shuzhengit.zhixin.rx.NetFlowableOnSubscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/9/29 09:18
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class AddAnswerModel implements AddAnswerContract.Model {
    @Override
    public Flowable<BaseCallModel> postAnswer(String answer, int questionId, int memberId, List<DocumentPicture>
            pictures) {
        return Flowable.create(new FlowableOnSubscribe<BaseCallModel>() {
            @Override
            public void subscribe(FlowableEmitter<BaseCallModel> e) throws Exception {
                JSONArray jsonArray = new JSONArray();
                for (DocumentPicture picture : pictures) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("ref", picture.getRef());
                    jsonObject.put("src", picture.getSrc());
                    jsonObject.put("alt", picture.getAlt());
                    jsonObject.put("pixel", picture.getPixel());
                    jsonArray.put(jsonObject);
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", answer);
                jsonObject.put("replayUserId", memberId);
                jsonObject.put("questionId", questionId);
                jsonObject.put("commentSwitch", 0);
                jsonObject.put("allPic", jsonArray);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),
                        jsonObject.toString());
                HttpProtocol.getApi()
                        .postAnswer(requestBody)
                        .subscribe(new NetFlowableOnSubscribe<BaseCallModel>(e));
//                        .subscribeWith(new DisposableSubscriber<BaseCallModel>() {
//                            @Override
//                            public void onNext(BaseCallModel callModel) {
//                                e.onNext(callModel);
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//                                e.onError(t);
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                e.onComplete();
//                            }
//                        });
            }
        }, BackpressureStrategy.BUFFER);
    }
}
