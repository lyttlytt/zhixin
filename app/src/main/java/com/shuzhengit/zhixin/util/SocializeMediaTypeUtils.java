package com.shuzhengit.zhixin.util;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/1 15:10
 * E-mail:yuancongbin@gmail.com
 */

public class SocializeMediaTypeUtils {
    public static final int GOOGLEPLUS = 0;
    public static final int GENERIC = 1;
    public static final int SMS = 2;
    public static final int EMAIL = 3;
    public static final int SINA = 4;
    public static final int QZONE = 5;
    public static final int QQ = 6;
    public static final int RENREN = 7;
    public static final int WEIXIN = 8;
    public static final int WEIXIN_CIRCLE = 9;
    public static final int WEIXIN_FAVORITE = 10;
    public static final int TENCENT = 11;
    public static final int DOUBAN = 12;
    public static final int FACEBOOK = 13;
    public static final int FACEBOOK_MESSAGER = 14;
    public static final int TWITTER = 15;
    public static final int LAIWANG = 16;
    public static final int LAIWANG_DYNAMIC = 17;
    public static final int YIXIN = 18;
    public static final int YIXIN_CIRCLE = 19;
    public static final int INSTAGRAM = 20;
    public static final int PINTEREST = 21;
    public static final int EVERNOTE = 22;
    public static final int POCKET = 23;
    public static final int LINKEDIN = 24;
    public static final int FOURSQUARE = 25;
    public static final int YNOTE = 26;
    public static final int WHATSAPP = 27;
    public static final int LINE = 28;
    public static final int FLICKR = 29;
    public static final int TUMBLR = 30;
    public static final int ALIPAY = 31;
    public static final int KAKAO = 32;
    public static final int DROPBOX = 33;
    public static final int VKONTAKTE = 34;
    public static final int DINGTALK = 35;
    public static SHARE_MEDIA[] transformShareMedia(int[] socializeMediaTypes){
        SHARE_MEDIA[] share_medias = new SHARE_MEDIA[socializeMediaTypes.length];
        for (int i = 0; i < socializeMediaTypes.length; i++) {
            share_medias[i] = transformMedia(socializeMediaTypes[i]);
        }
        return share_medias;
    }

    private static SHARE_MEDIA transformMedia(int socializeMediaType) {
        switch (socializeMediaType){
            case GOOGLEPLUS:return SHARE_MEDIA.GOOGLEPLUS;
            case GENERIC:return SHARE_MEDIA.GENERIC;
            case SMS:return SHARE_MEDIA.SMS;
            case EMAIL:return SHARE_MEDIA.EMAIL;
            case SINA:return SHARE_MEDIA.SINA;
            case QZONE:return SHARE_MEDIA.QZONE;
            case QQ:return SHARE_MEDIA.QQ;
            case RENREN:return SHARE_MEDIA.RENREN;
            case WEIXIN:return SHARE_MEDIA.WEIXIN;
            case WEIXIN_CIRCLE:return SHARE_MEDIA.WEIXIN_CIRCLE;
            case WEIXIN_FAVORITE:return SHARE_MEDIA.WEIXIN_FAVORITE;
            case TENCENT:return SHARE_MEDIA.TENCENT;
            case DOUBAN:return SHARE_MEDIA.DOUBAN;
            case FACEBOOK:return SHARE_MEDIA.FACEBOOK;
            case FACEBOOK_MESSAGER:return SHARE_MEDIA.FACEBOOK_MESSAGER;
            case TWITTER:return SHARE_MEDIA.TWITTER;
            case LAIWANG:return SHARE_MEDIA.LAIWANG;
            case LAIWANG_DYNAMIC:return SHARE_MEDIA.LAIWANG_DYNAMIC;
            case YIXIN:return SHARE_MEDIA.YIXIN;
            case YIXIN_CIRCLE:return SHARE_MEDIA.YIXIN_CIRCLE;
            case INSTAGRAM:return SHARE_MEDIA.INSTAGRAM;
            case PINTEREST:return SHARE_MEDIA.PINTEREST;
            case EVERNOTE:return SHARE_MEDIA.EVERNOTE;
            case POCKET:return SHARE_MEDIA.POCKET;
            case LINKEDIN:return SHARE_MEDIA.LINKEDIN;
            case FOURSQUARE:return SHARE_MEDIA.FOURSQUARE;
            case YNOTE:return SHARE_MEDIA.YNOTE;
            case WHATSAPP:return SHARE_MEDIA.WHATSAPP;
            case LINE:return SHARE_MEDIA.LINE;
            case FLICKR:return SHARE_MEDIA.FLICKR;
            case TUMBLR:return SHARE_MEDIA.TUMBLR;
            case ALIPAY:return SHARE_MEDIA.ALIPAY;
            case KAKAO:return SHARE_MEDIA.KAKAO;
            case VKONTAKTE:return SHARE_MEDIA.VKONTAKTE;
            case DROPBOX:return SHARE_MEDIA.DROPBOX;
            case DINGTALK:return SHARE_MEDIA.DINGTALK;
            default:throw new IllegalArgumentException("socializeMediaType is error");
        }
    }
}
