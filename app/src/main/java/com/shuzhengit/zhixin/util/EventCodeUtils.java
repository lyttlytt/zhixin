package com.shuzhengit.zhixin.util;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/7/20 09:03
 * E-mail:yuancongbin@gmail.com
 */
//1.不可编辑的的状态下 点击我的栏目 直接finish 通过rxbus通知inndexfragment切换到相应的栏目
// 2.长按可编辑的状态下切换位置
// 在finish前通过rxbus通知fragment更新栏目集合/notify?
// 3.点击推荐的栏目直接添加到我关注的栏目 finish之前手动增加集合并更新服务器数据
// 4.删除我的关注,finish之前手动remove集合,
// 再异步更新服务器数据
public class EventCodeUtils {
    public static final String DOCUMENT_FRAGMENT_VALUE = "data";
    public static final String IS_HISTORY = "show_history";
    public static final String REFRESH_USER = "refreshUser";
    public static final Integer REFRESH_USER_INT = 101;
    public static final Integer CLICK_COLUMN = 102;
    public static final Integer MODIFY_COLUMN=103;
    public static final Integer ISMODIFY_CLICK_COLUMN=104;
    public static final String DOCUMENT_ID="documentId";
    public static final String COMMENT_ID="commentId";
    public static final String DOCUMENT_PICTURE="document_picture";
    public static final String QUESITON_TYPE="questionType";

    public static final Integer REFRESH_ANSWER=105;
    public static final Integer REFRESH_QUESTIONS=106;
    public static final Integer RELEASE_SUBSCRIBER=107;
    public static final Integer RELEASE_REFRESH_QUESTION = 108;
    public static final Integer RELEASE_REFRESH_QUESTION_ANSWER = 109;

    public static final String COLUMN_LOCAL_SCHOOL="localSchool";
    public static final String COLUMN_LOCAL_CITY="localCity";

    public static final Integer USER_SCHOOL_ID=110;
    public static final Integer REFRESH_REPLYS=111;
    public static final Integer WEN_BA_CATEGORY=112;
    public static final Integer REFRESH_WENBA=113;
}
