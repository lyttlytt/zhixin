package com.shuzhengit.zhixin.http;

import com.library.bean.BaseCallModel;
import com.shuzhengit.zhixin.bean.AnswerComment;
import com.shuzhengit.zhixin.bean.ApplyAdmin;
import com.shuzhengit.zhixin.bean.AskComment;
import com.shuzhengit.zhixin.bean.AskWithReply;
import com.shuzhengit.zhixin.bean.Basic;
import com.shuzhengit.zhixin.bean.CheckVersion;
import com.shuzhengit.zhixin.bean.Column;
import com.shuzhengit.zhixin.bean.Comment;
import com.shuzhengit.zhixin.bean.DataContainer;
import com.shuzhengit.zhixin.bean.Document;
import com.shuzhengit.zhixin.bean.Dynamic;
import com.shuzhengit.zhixin.bean.IPModel;
import com.shuzhengit.zhixin.bean.ModifyBarInfo;
import com.shuzhengit.zhixin.bean.Question;
import com.shuzhengit.zhixin.bean.QuestionAnswer;
import com.shuzhengit.zhixin.bean.QuestionCategory;
import com.shuzhengit.zhixin.bean.QuestionTag;
import com.shuzhengit.zhixin.bean.School;
import com.shuzhengit.zhixin.bean.Token;
import com.shuzhengit.zhixin.bean.UploadImg;
import com.shuzhengit.zhixin.bean.User;
import com.shuzhengit.zhixin.bean.WenBa;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/26 17:10
 * E-mail:yuancongbin@gmail.com
 */

public interface ApiService {
//    @Headers("cache:30")
 //  @GET("http://api.map.baidu.com/geocoder?output=json&location={经度},{纬度}&key=pRO8usOeq1D4WRCtC5w1eOEfPb0X9Sa3");
//    Flowable<BaseCallModel> findLocationCity(@Query("output") String outputFormat, @Query
//            ("location") String location, @Query("key") String baiduKey);

    //获取匿名用户栏目
    //@GET("column/isLikeColumn");
//    Flowable<BaseCallModel<PageContainer<Column>>> findDefaultColumns();

    //获取用户关注的栏目  匿名用户带0
    @GET("/api/v1/cms-service/column/findMemberLikeColumn/{memberId}")
    Flowable<BaseCallModel<List<Column>>> findUserFollowColumns(@Path("memberId") int memberId);

    //根据定位城市获取文章
    @GET("/api/v1/cms-service/document/{location}/location")
    Flowable<BaseCallModel<List<Document>>> findLocalCityDocuments(@Path("location") String city, @Query("pageNum")
            int page, @Query("pageSize") int pageSize);

    //根据学校获取文章
    @GET("/api/v1/cms-service/document/{schoolId}/school")
    Flowable<BaseCallModel<List<Document>>> findLocalSchoolDocuments(@Path("schoolId") int schoolId, @Query("pageNum")
            int page, @Query("pageSize") int pageSize);

    //根据学校的id获取新闻
    @GET("/api/v1/cms-service/document/es/school/{schoolCode}")
    Flowable<BaseCallModel<List<Document>>> findDocumentBySchoolId
    (@Path("schoolCode") int schoolCode, @Query("pageNum") int page, @Query("pageSize") int pageSize);

    //获取推荐栏目
    @GET("/api/v1/cms-service/document/wechat")
    Flowable<BaseCallModel<List<Document>>> findRecommendDocument(@Query("pageNum") int page, @Query("pageSize") int
            pageSize);

    //根据学校的id获取周边新闻
    @GET("/api/v1/cms-service/document/es/periphery/{schoolCode}")
    Flowable<BaseCallModel<List<Document>>> findZhouBianDocumentBySchoolId(@Path("schoolCode") int schoolCode, @Query
            ("pageNum") int page, @Query("pageSize") int pageSize);

    //获取用户未关注的栏目
    @GET("/api/v1/cms-service/column/findMemberDislikeColumn/{memberId}")
    Flowable<BaseCallModel<List<Column>>> findUserUnFollowColumns(@Path("memberId") int memberId);

//    //根据用户memberId获取对应的标签
//    @GET("tag/findTagByMemberId/{memberId}")
//    Flowable<BaseCallModel<List<Channel>>> findChannelByUserMember(@Path("memberId") int memberId);

//    //根据栏目的title 分页获取新闻列表
//    @GET("document/column/{columnTitle}")
//    Flowable<BaseCallModel<List<Document>>> findNewsByColumnTitle(@Path("columnTitle") String columnTitle,
//                                                                  @Query("page") int page, @Header("forceNetwork")
//                                                                          boolean forceNetwork);

    //根据栏目的id 分页获取新闻列表
//    @GET("/api/v1/cms-service/document/column/{column}/docTitle")
    @GET("/api/v1/cms-service/document/es/{code}")
//    Flowable<BaseCallModel<List<Document>>> findDocumentByColumnId(@Path("column") int columnTitle, @Query("pageNum")
//            int page, @Query("pageSize") int pageNum);
    Flowable<BaseCallModel<List<Document>>> findDocumentByColumnId(@Path("code") String columnCode, @Query("pageNum")
            int page, @Query("pageSize") int pageNum);

    //根据文章id获取详情
//    @GET("/api/v1/cms-service/admin/document/{documentId}/{memberId}")
    @GET("/api/v1/cms-service/document/elc/{esId}/{memberId}")
    Flowable<BaseCallModel<Document>> findDocumentDetailByDocumentId(@Path("esId") String esId, @Path
            ("memberId") int userId);
//    Flowable<BaseCallModel<Document>> findDocumentDetailByDocumentId(@Path("esId") int documentId, @Path
//            ("memberId") int userId);


    //批量更新栏目 body包含sortNo排序、columnId 栏目的id
    @POST("/api/v1/cms-service/column/likeOrDislikeColumns/{memberId}")
    Flowable<BaseCallModel<String>> batchUpdateColumn(@Path("memberId") int memberId, @Body RequestBody updateColumns);

    //分页获取指定新闻的一级评论
    @GET("/api/v1/cms-service/documentcomment/findByDocumentId/{documentId}/{memberId}")
    Flowable<BaseCallModel<List<Comment>>> findCommentByPage(@Path("documentId") int documentId, @Path("memberId") int
            memberId, @Query("pageNum") int page, @Query("pageSize") int totalSize);

    //根据评论id获取详细评论
    @GET("/api/v1/cms-service/documentcomment/{commentId}/{memberId}")
    Flowable<BaseCallModel<Comment>> findCommentById(@Path("commentId") int commentId
            , @Path("memberId") int memberId);

    //分页获取指定一级评论下的二级评论
    @GET("/api/v1/cms-service/documentcomment/children/{commendId}/{memberId}")
    Flowable<BaseCallModel<List<Comment>>> findSecondaryComment(
            @Path("commendId") int commentId, @Path("memberId") int memberId, @Query("pageNum")
            int pageNum, @Query("pageSize") int pageSize);


    /**
     * @param comment commentContent 评论内容
     * @param comment commentUserId 用户id
     * @param comment documentId  一级评论 id是文档的id/ 二级评论是一级评论的id
     * @param comment upperCommentId 一级评论是0 ／ 二级评论是一级评论的id
     * @return
     */
    //评论
    @POST("/api/v1/cms-service/documentcomment/addComment")
    Flowable<BaseCallModel<Comment>> comment(@Body RequestBody comment);


    //收藏
    @POST("/api/v1/cms-service/favourite/favoriteOrNot")
    Flowable<BaseCallModel> favouriteOrNotDocument(@Body RequestBody collect);

    //收藏列表
    @GET("/api/v1/cms-service/favourite/{memberId}/columns")
    Flowable<BaseCallModel<List<Document>>> findDocumentsByFavourite(
            @Path("memberId") int memberId,
            @Query("pageNum") int pageNum,
            @Query("pageSize") int pageSize);

    //浏览历史
    @GET("/api/v1/cms-service/readhistory/member/{memberId}")
    Flowable<BaseCallModel<List<Document>>> findDocumentByHistory(
            @Path("memberId") int memberId,
            @Query("pageNum") int pageNum,
            @Query("pageSize") int pageSize);

    //添加浏览历史
    @POST("/api/v1/cms-service/readhistory")
    Flowable<BaseCallModel> readHistory(@Body RequestBody body);

    //给文章点赞
    @POST("/api/v1/cms-service/documentlike/likeOrDislikeDocument")
    Flowable<BaseCallModel> documentLike(@Body RequestBody body);

    //给评论点赞
    @POST("/api/v1/cms-service/commentlike/likeOrDislikeComment")
    Flowable<BaseCallModel<String>> commentLike(@Body RequestBody body);

    //用户注册验证码
    @GET("/api/v1/cms-service/validatecode")
    Flowable<BaseCallModel> findValidateCode(@Query("phone") String phone);

    //用户注册
    @POST("/api/v1/user-service/user/sign_up")
    Flowable<BaseCallModel> register(@Body RequestBody body);

    //用户登录
    // grant_type:password、qq、wechat
    // username:密码登录账号照常 三方随意、
    // password:密码登录照常、三方登录thirdparty
    // openid:三方登录带上 密码登录随意
    // access_token:三方登录带上 密码登录随意
    // auth_pf_type:password、qq、wechat
    //三方获取token
    @FormUrlEncoded
    @POST("/api/v1/oauth-service/oauth/token")
    Flowable<Token> socialLogin(@Field("client_id") String clientId,
                                @Field("client_secret") String secret,
                                @Field("grant_type") String grantType,
                                @Field("username") String username,
                                @Field("password") String password,
                                @Field("openid") String openId,
                                @Field("access_token") String access_token,
                                @Field("auth_pf_type") String authPfType);

    //账号密码
    @FormUrlEncoded
    @POST("/api/v1/oauth-service/oauth/token")
    Flowable<Token> passwordLogin(@Field("client_id") String clientId,
                                  @Field("client_secret") String secret,
                                  @Field("grant_type") String grantType,
                                  @Field("username") String username,
                                  @Field("password") String password);

    // grant_type : refresh_token
    // username : 三方用openid 否则用账号
    // password : 三方thirdparty 否则用密码
    // client_id:固定 和获取token一样
    // clint_secret:固定 和获取token一样
    // refresh_token:过期token中的refresh_token
    //刷新token
    @FormUrlEncoded
    @POST("/api/v1/oauth-service/oauth/token")
    Flowable<Token> refreshToken(@Field("grant_type") String grantType,
                                 @Field("client_id") String clientId,
                                 @Field("client_secret") String clientSecret,
                                 @Field("refresh_token") String refresh_token);

    //刷新token
    @FormUrlEncoded
    @POST("/api/v1/oauth-service/oauth/token")
    Call<Token> refreshTokenSynchronized(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("refresh_token") String refresh_token);

    //修改用户头像
    @Multipart
    @POST("/api/v1/user-service/user/avatar")
    Flowable<BaseCallModel<String>> updateAvatar(
//            @Header("AccessToken") String token,
            @Header("uid") String userId,
            @Part("file\"; filename=\"avatar.png") RequestBody avatar);

    //根据id获取用户信息
//    @GET(HttpProtocol.sUserUrl + "/api/v1/user-service/user/{account}/account")
    @GET("/api/v1/user-service/user/{id}/id")
    Flowable<BaseCallModel<User>> findUserInfoById(@Header("uid") int userMemberId, @Path("id") String queryMemberId);

    //修改用户信息
    @PUT("/api/v1/user-service/user")
    Flowable<BaseCallModel<Object>> modifyUserInfo(
            @Header("uid") String uid,
            @Body RequestBody requestBody);

    @PUT("/api/v1/user-service/user")
    Flowable<BaseCallModel<Double>> modifyUserInfoGender(
            @Header("uid") String uid,
            @Body RequestBody requestBody);

    //修改昵称
    //{"id":7,"nickname":"昵称"}
    @POST("/api/v1/cms-service//member/modifyNickname")
    Flowable<BaseCallModel<User>> modifyNickName(@Body RequestBody body);

    //根据token获取基本信息
    @GET("/api/v1/oauth-service/oauth/get_auth")
    Flowable<BaseCallModel<Basic>> findUserInfoByToken(
            @Query("token") String token);

    //根据token获取基本信息
    @GET("/api/v1/oauth-service/oauth/get_auth")
    Call<BaseCallModel<Basic>> findInfoByTokenCall(
//            @Header("AccessToken") String accessToken,
            @Query("token") String token);

    //发送验证码
    @GET("/api/v1/user-service/user/sign_up/{mobile}/validate_code")
    Flowable<BaseCallModel> sendCode(@Path("mobile") String mobile);

    //重置密码中的发送验证码
    @GET("/api/v1/user-service/user/reset_password/{mobile}/validate_code")
    Flowable<BaseCallModel> sendResetPasswordValidCode(@Path("mobile") String password);

    //重置密码
    @PUT("/api/v1/user-service/user/reset_password")
    Flowable<BaseCallModel<User>> putResetPassword(@Body RequestBody body);

    //根据定位的城市获取学校
    @GET("/api/v1/cms-service/school/baiduCityCode/{baiduCityCode}")
    Flowable<BaseCallModel<List<School>>> findSchoolByBaiduCityCode(@Path("baiduCityCode") String baiduCityCode);

    //模糊搜索  查询学校
    @GET("/api/v1/cms-service/school/name/{info}")
    Flowable<BaseCallModel<List<School>>> findSchoolByLike(@Path("info") String info);

    //用户反馈
    @POST("/api/v1/cms-service/feedback")
    Flowable<BaseCallModel> sendFeedback(@Body RequestBody body);


    //关注用户
    @POST("/api/v1/user-service/user/fans/follow")
    Flowable<BaseCallModel> followUser(@Body RequestBody requestBody);

    //取消关注用户
    @POST("/api/v1/user-service/user/fans/unfollow")
    Flowable<BaseCallModel> unFollowUser(@Body RequestBody requestBody);

    //获取粉丝数的列表
    @GET("/api/v1/user-service/user/fans/{id}/page/{page}_{pageSize}")
    Flowable<BaseCallModel<DataContainer<User>>> findFansById(@Path("id") int memberId, @Path("page") int page, @Path
            ("pageSize") int pageSize);

    //获取关注列表
    @GET("/api/v1/user-service/user/fans/follow/{id}/page/{page}_{pageSize}")
    Flowable<BaseCallModel<DataContainer<User>>> findFollowById(@Path("id") int memberId, @Path("page") int page, @Path
            ("pageSize") int pageSize);

    //获取用户动态
    @GET("/api/v1/cms-service/trends/{memberId}/{page}_{pageNum}/page")
    Flowable<BaseCallModel<List<Dynamic>>> findDynamics(@Path("memberId") String memberId, @Path("page") int page,
                                                        @Path("pageNum") int pageNum);

    //发布一个问题
//    http://192.168.1.175:9001
    @POST("/api/v1/cms-service/qaquestion")
    Flowable<BaseCallModel<Question>> releaseQuestion(@Body RequestBody requestBody);

    //获取问答的分类
    @GET("/api/v1/cms-service/qacategory/0_0/page")
    Flowable<BaseCallModel<DataContainer<QuestionCategory>>> findQuestionCategory();

    //根据分类的类型id获取问答列表
    @GET("/api/v1/cms-service/qaquestion/{page}_{pageNum}/{category}/page")
    Flowable<BaseCallModel<DataContainer<Question>>> findQuestionByCategoryId(
            @Path("page") int page, @Path("pageNum") int pageNum,
            @Path("category") int category);

    //根据问题标题关键字 查询相关标签
    @GET("/api/v1/cms-service/qatag/like/{title}")
    Flowable<BaseCallModel<List<QuestionTag>>> findQuestionKeyWords(@Path("title") String keyword);


    //标签与问题关联
    @FormUrlEncoded
    @POST("/api/v1/cms-service/qalinktag/saveMore")
    Flowable<BaseCallModel> questionRelevanceTag(@Field("jsonData") String data);

    //上传图片
    @Multipart
    @POST("/api/v1/user-service/oss/upload")
    Flowable<BaseCallModel<UploadImg>> uploadImg(@Part("file\"; filename=\"img.png") RequestBody body);

//    @Multipart
//    @POST("/api/v1/user-service/oss/upload")
//    Call<BaseCallModel<UploadImg>> uploadImg1(@Part("file\"; filename=\"img.png") RequestBody body);


    //获取问题详情
    @GET("/api/v1/cms-service/qaquestion/{id}/{memberId}")
    Flowable<BaseCallModel<Question>> findQuestionDetail(@Path("id") int questionId, @Path("memberId") int memberId);

    //分页获取答案
    @GET("/api/v1/cms-service/qaanswer/{pageNum}_{pageSize}/{questionId}/page/{userId}")
    Flowable<BaseCallModel<DataContainer<QuestionAnswer>>> fetchQuestionAnswer(@Path("pageNum") int page, @Path
            ("pageSize") int
            pageSize, @Path("questionId") int questionId, @Path("userId") int memberId);

    //根据回答的id获取详情
    @GET("/api/v1/cms-service/qaanswer/{id}")
    Flowable<BaseCallModel<QuestionAnswer>> fetchAnswerDetailById(@Path("id") int id);

    //给一个答案点赞
    @POST("/api/v1/cms-service/qaanswervoterecord/vote")
    Flowable<BaseCallModel> agreeAnswer(@Body RequestBody requestBody);

    //添加问题浏览记录
    @POST("/api/v1/cms-service/qaquestionreadrecord")
    Flowable<BaseCallModel> addHistoryQuestion(@Body RequestBody requestBody);

    //添加答案浏览记录
    @POST("/api/v1/cms-service/qaanswerreadrecord")
    Flowable<BaseCallModel> addHistoryAnswer(@Body RequestBody requestBody);

    //分页获取回答下的评论
    @GET("/api/v1/cms-service/qacomment/{pageNum}_{pageSize}/{answerId}/page/{userId}")
    Flowable<BaseCallModel<DataContainer<AnswerComment>>> fetchAnswerComment(@Path("pageNum") int page, @Path
            ("pageSize") int pageNum, @Path("answerId") int answerId, @Path("userId") int userId);

    //关注一个问题
    @POST("/api/v1/cms-service/qalikerecord/findRecord")
    Flowable<BaseCallModel> followedQuestion(@Body RequestBody requestBody);

    //添加回答
    @POST("/api/v1/cms-service/qaanswer")
    Flowable<BaseCallModel> postAnswer(@Body RequestBody body);

    //给回答添加一个评论
    @POST("/api/v1/cms-service/qacomment")
    Flowable<BaseCallModel<AnswerComment>> addAnswerComment(@Body RequestBody requestBody);

    //问答浏览记录
    @GET("/api/v1/cms-service/qaquestionreadrecord/{pageNum}_{pageSize}/{userId}/page")
    Flowable<BaseCallModel<DataContainer<Question>>> findQuestionHistoryByUserId(@Path("pageNum") int page, @Path
            ("pageSize") int pageSize, @Path("userId") int userId);

    //根据回答的id分页获取评论
    @GET("/api/v1/cms-service/qacomment/{pageNum}_{pageSize}/{answerId}/page/{userId}")
    Flowable<BaseCallModel<DataContainer<AnswerComment>>> fetchAnswerCommentByAnswerId(
            @Path("pageNum") int page,
            @Path("pageSize") int pageSize,
            @Path("answerId") int answerId,
            @Path("userId") int userId);

    //    //对回答下的评论点赞
    @POST("/api/v1/cms-service/qaanswercommentvoterecord")
    Flowable<BaseCallModel> agreeAnswerComment(@Body RequestBody requestBody);

    //分页获取我提出的和回答的问题
//   type 1提问 2回答
    @GET("/api/v1/cms-service/qaquestion/trends/{pageNum}_{pageSize}/{userId}_{type}/page")
    Flowable<BaseCallModel<DataContainer<Question>>> fetchMineQuestionByPage(@Path("pageNum") int page, @Path
            ("pageSize") int
            pageSize, @Path("userId") int userId, @Path("type") int type);

    //获取动态的提问和回答
    @GET("/api/v1/cms-service/qaquestion/allTrends/{pageNum}_{pageSize}/{userId}/page")
    Flowable<BaseCallModel<DataContainer<Question>>> fetchDynamicQuestions(@Path("pageNum") int page,
                                                                           @Path("pageSize")
                                                                                   int pageSize, @Path("userId") int
                                                                                   userId);

    //分页获取用户关注的问答
    @GET("/api/v1/cms-service/qaquestion/like/{pageNum}_{pageSize}/{userId}/page")
    Flowable<BaseCallModel<DataContainer<Question>>> fetchUserFollowQuestion(@Path("pageNum") int page, @Path
            ("pageSize") int pageSize, @Path("userId") int userId);


    //检查版本信息
    //没有更新 code 304 data = null
    @GET("/api/v1/user-service/app/version/check/{versionCode}")
    Flowable<BaseCallModel<CheckVersion>> checkVersion(@Path("versionCode") int versionCode);


    //    @GET("{downloadUrl}")
//    Call<RequestBody> loadFile(@Path("downloadUrl") String downloadUrl);
//    @GET("http://ox36wigei.bkt.clouddn.com/app-baidu-release.apk")
    @GET
    Call<ResponseBody> loadFile(@Url String download);


    // ============= 问吧 ================
    //问吧列表
    @GET("/api/v1/cms-service/qabar/{page}_{pageSize}/{userId}/page")
    Flowable<BaseCallModel<DataContainer<WenBa>>> fetchWenBaLists(@Path("page") int page, @Path("pageSize") int
            pageSize, @Path("userId") int userId);

    //问吧详情
    @GET("/api/v1/cms-service/qabar/{wenBaId}/{userId}")
    Flowable<BaseCallModel<WenBa>> fetchWenBaDetail(@Path("wenBaId") int wenBaId, @Path("userId") int userId);

    //    {
//        "answerUserId": 1,
//            "description": "请问抑郁症怎么办",
//            "userId": 4
//    }
    //提问
    @POST("/api/v1/cms-service/qaquestion")
    Flowable<BaseCallModel> postAsk(@Body RequestBody body);

    //分页获取提问和回复
    //type 0最新 1 最热
    @GET("/api/v1/cms-service/qaquestion/{page}_{pageSize}/{wenBaId}/{userId}/page/{type}")
    Flowable<BaseCallModel<DataContainer<AskWithReply>>> fetchAskWithReply(@Path("page") int page, @Path("pageSize")
            int pageSize, @Path("wenBaId") int wenBaId, @Path("userId") int userId, @Path("type") int type);

    //对单个提问的评论
    @POST("/api/v1/cms-service/qacomment")
    Flowable<BaseCallModel> postComment(@Body RequestBody body);

    //关注问吧
    @POST("/api/v1/cms-service/qalikerecord/findRecord")
    Flowable<BaseCallModel> followWenBa(@Body RequestBody body);

    //分页获取我关注的问吧
    @GET("/api/v1/cms-service/qabar/myLike/{page}_{pageSize}/{userId}/page")
    Flowable<BaseCallModel<DataContainer<WenBa>>> fetchMineLikeWenBa(@Path("page") int page, @Path("pageSize") int
            pageSize, @Path("userId") int userId);

    //分页获取我提问的列表
    @GET("/api/v1/cms-service/qaquestion/myQuestion/{page}_{pageSize}/{userId}")
    Flowable<BaseCallModel<DataContainer<AskWithReply>>> fetchAsks(@Path("page") int page, @Path("pageSize") int
            pageSize, @Path
                                                                           ("userId") int userId);

    //分页获取吧主待回答列表
    @GET("/api/v1/cms-service/qaquestion/barQuestion/{page}_{pageSize}/{userId}/page")
    Flowable<BaseCallModel<DataContainer<WenBa>>> fetchAdminUnReplys(@Path("page") int page, @Path("pageSize") int
            pageSize, @Path("userId") int userId);

    //对提问进行回复
    @POST("/api/v1/cms-service/qaanswer")
    Flowable<BaseCallModel> postReply(@Body RequestBody body);

    //检查当前用户是不是吧主
    @GET("/api/v1/cms-service/qabar/isBar/{userId}")
    Flowable<BaseCallModel<ApplyAdmin>> checkIsAdmin(@Path("userId") int userId);

    //给提问点赞
    @POST("/api/v1/cms-service/qaanswervoterecord/vote")
    Flowable<BaseCallModel> agreeAsk(@Body RequestBody body);

    //根据id获取单个提问和回复的详情
    @GET("/api/v1/cms-service/qaquestion/{askId}/id/{userId}")
    Flowable<BaseCallModel<AskWithReply>> findAskWithReplyById(@Path("askId") int askId, @Path("userId") int userId);

    //分页获取评论列表
    @GET("/api/v1/cms-service/qacomment/{page}_{pageSize}/{askId}/page/{userId}")
    Flowable<BaseCallModel<DataContainer<AskComment>>> fetchAskComments(@Path("page") int page, @Path("pageSize")
            int pageSize, @Path("askId") int askId, @Path("userId") int userId);

    //给评论点赞
    @POST("/api/v1/cms-service/qaanswercommentvoterecord")
    Flowable<BaseCallModel> agreeAskComment(@Body RequestBody body);

    //问吧浏览历史
    @GET("/api/v1/cms-service/qabar/history/{page}_{pageSize}/{userId}")
    Flowable<BaseCallModel<DataContainer<WenBa>>> fetchHistoryWenBa(@Path("page") int page, @Path("pageSize") int
            pageSize, @Path("userId") int userId);

    //问吧分类
    @GET("/api/v1/cms-service/column?pageNum=0&pageSize=0")
    Flowable<BaseCallModel<List<Column>>> findCategory();

    //
    //申请吧主
    @POST("/api/v1/cms-service/qabar")
    Flowable<BaseCallModel> applyAdmin(@Body RequestBody body);

    //登录成功后发送用户数据统计
//    @PUT("http://192.168.1.102:9999/api/v1/user-service/user/login_success")
    @PUT("/api/v1/user-service/user/login_success")
    Flowable<BaseCallModel> postUserData(@Header("uid") int uid, @Body RequestBody body);

    //栏目返回null
//    @POST("http://192.168.1.102:9999/api/v1/user-service/user/re_subscribe")
    @POST("/api/v1/user-service/user/re_subscribe")
    Flowable<BaseCallModel> reSubscriberColumn(@Header("uid") int uid, @Header("Content-Type") String contentType);

    //淘宝ip
    @GET("http://ip.taobao.com/service/getIpInfo.php?ip=myip")
    Flowable<IPModel> getIP(@Header("User-Agent") String userAgent);

    //提交修改问吧信息
    @POST("/api/v1/cms-service/qabarcheck")
    Flowable<BaseCallModel> modifyWenBaInfo(@Body RequestBody body);

    //根据用户id查询修改状态
    @GET("/api/v1/cms-service/qabarcheck/findRecord/{userId}")
    Flowable<BaseCallModel<ModifyBarInfo>> queryWenBaStatus(@Path("userId") int userId);
}
