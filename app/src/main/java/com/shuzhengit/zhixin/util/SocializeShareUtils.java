package com.shuzhengit.zhixin.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.library.permission.AcpListener;
import com.library.permission.AcpOptions;
import com.library.permission.AcpUtils;
import com.library.util.LogUtils;
import com.library.util.ToastUtils;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.view.SocializeLoadingDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/8/1 14:07
 * E-mail:yuancongbin@gmail.com
 */

public class SocializeShareUtils {
    private SHARE_MEDIA[] mSocialMedia;
    private String mTitle;
    private String mDescription;
    private String mUrl;
    private String mThumb;
    private UMWeb mUmWeb;

    private SocializeShareUtils(UMWeb umWeb, SHARE_MEDIA[] socialMedia) {
        mUmWeb = umWeb;
        this.mSocialMedia = socialMedia;
    }

//     abstract class ShareListener{
//        public void onStart() {
//        }
//
//        public void onResult() {
//        }
//
//        public void onError(Context context, String media, Throwable throwable) {
//            if (TextUtils.isEmpty(media)) {
//                ToastUtils.showShortToast(context, throwable.getMessage());
//            } else {
//                if (media.equals("QQ")) {
//                    boolean qqAvailable = ShareMediaAvailableUtils.isQQAvailable(APP
//                            .getInstance());
//                    if (!qqAvailable) {
//                        ToastUtils.showShortToast(context, "请先安装QQ");
//                    }
//                }
//                if (media.equals("WEIXIN") || media.equals("WEIXIN_CIRCLE")) {
//                    boolean weChatAvailable = ShareMediaAvailableUtils.isWeChatAvailable(APP
//                            .getInstance());
//                    if (!weChatAvailable) {
//                        ToastUtils.showShortToast(context, "请安装微信");
//                    }
//                }
//            }
//        }
//
//        public void onCancel() {
//        }
//    }
    public static class ShareListener {
        SocializeLoadingDialog mSocializeLoadingDialog;
        public void onStart(Context context) {
            mSocializeLoadingDialog = new SocializeLoadingDialog(context);
            mSocializeLoadingDialog.show();
        }

        public void onResult(Context context) {
            if (mSocializeLoadingDialog!=null){
                mSocializeLoadingDialog.dismiss();
            }
        }

        public void onError(Context context, String media, Throwable throwable) {
            if (TextUtils.isEmpty(media)) {
                ToastUtils.showShortToast(context, throwable.getMessage());
            } else {
                if ("QQ".equals(media)) {
                    boolean qqAvailable = ShareMediaAvailableUtils.isQQAvailable(APP
                            .getInstance());
                    if (!qqAvailable) {
                        ToastUtils.showShortToast(context, "请先安装QQ");
                    }
                }
                if ("WEIXIN".equals(media) || "WEIXIN_CIRCLE".equals(media)) {
                    boolean weChatAvailable = ShareMediaAvailableUtils.isWeChatAvailable(APP
                            .getInstance());
                    if (!weChatAvailable) {
                        ToastUtils.showShortToast(context, "请安装微信");
                    }
                }
            }
        }

        public void onCancel(Context context) {
            if (mSocializeLoadingDialog!=null){
                mSocializeLoadingDialog.dismiss();
            }
        }
    }

    public void openShare(Activity activity, ShareListener shareListener) {
        AcpUtils.getInstance(APP.getInstance())
                .request(new AcpOptions.Builder().setPermissions(Manifest.permission_group.STORAGE).build(), new
                        AcpListener() {
                            @Override
                            public void onGranted() {
                                new ShareAction(activity)
                                        .setDisplayList(mSocialMedia)
                                        .withMedia(mUmWeb)
                                        .setCallback(new UMShareListener() {

                                            @Override
                                            public void onStart(SHARE_MEDIA share_media) {
                                                LogUtils.i("umengsocial", share_media.toString());
                                                shareListener.onStart(activity);
                                            }

                                            @Override
                                            public void onResult(SHARE_MEDIA share_media) {
                                                LogUtils.i("umengsocial", share_media.toString());
                                                shareListener.onResult(activity);
                                            }

                                            @Override
                                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                LogUtils.e("umengsocial", share_media.toString() + throwable
                                                        .getMessage());
                                                shareListener.onError(activity, share_media.toString(), throwable);
                                            }

                                            @Override
                                            public void onCancel(SHARE_MEDIA share_media) {
                                                LogUtils.i("取消");
                                                shareListener.onCancel(activity);
                                            }
                                        }).open();
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                shareListener.onError(activity, "", new Throwable("permission_group denied"));
                            }
                        });
    }
    public static class Builder {
        private SHARE_MEDIA[] mSocialMedia;
        private String mTitle;
        private String mDescription;
        private String mUrl;
        private String mThumb;
        private int mThumbId;
        private Bitmap mThumbBitmap;

//        public Builder(int... socializeMediaType, String title, String description, String url, String thumb) {
//            if (socializeMediaType==null || socializeMediaType.length==0){
//                throw new IllegalArgumentException("socialMedia cannot be null! 分享平台不能为空!");
//            }
//            mSocialMedia = SocializeMediaTypeUtils.transformShareMedia(socializeMediaType);
//            mTitle = title;
//            mDescription = description;
//            mUrl = url;
//            mThumb = thumb;
//        }

        public Builder setSocializeMedia(int... socializeMediaTypeUtil) {
            this.mSocialMedia = SocializeMediaTypeUtils.transformShareMedia(socializeMediaTypeUtil);
            return this;
        }

        //        UMWeb umWeb = new UMWeb("https://www.baidu.com/");
//            umWeb.setTitle("App");
//            umWeb.setDescription("美国麦当劳冰淇淋机事件仍在持续发酵。");
//            umWeb.setThumb(umImage);
        public Builder setUMWebTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setUMWebDescription(String description) {
            mDescription = description;
            return this;
        }

        public Builder setUMWebUrl(String url) {
            mUrl = url;
            return this;
        }

        public Builder setUMWebThumb(String thumb) {
            mThumb = thumb;
            return this;
        }

        public Builder setUMWebThumb(int resId) {
            mThumbId = resId;
            return this;
        }

        public Builder setUMWebThumb(Bitmap bitmap) {
            mThumbBitmap = bitmap;
            return this;
        }

        public Builder setUMWeb(UMWeb umWeb) {
            this.setUMWeb(umWeb);
            return this;
        }

        public SocializeShareUtils buildUMWeb(Context context) {
            if (TextUtils.isEmpty(mUrl)) {
                throw new IllegalArgumentException("UMWebUrl cannot be null! 分享的连接地址不能为空!");
            }
            if (mSocialMedia == null || mSocialMedia.length == 0) {
                throw new IllegalArgumentException("socialMedia cannot be null! 分享平台不能为空!");
            }
            if (TextUtils.isEmpty(mTitle)) {
                throw new IllegalArgumentException("UMWeb title cannot be null! UMWeb的title不能为空");
            }
            if (TextUtils.isEmpty(mDescription)) {
                throw new IllegalArgumentException("UMWeb description cannot be null! UMWeb的description不能为空");
            }
            if (TextUtils.isEmpty(mThumb) & mThumbBitmap == null & mThumbId == 0) {
                throw new IllegalArgumentException("UMWeb thumb cannot be null! UMWeb的thumb不能为空!");
            }

            UMWeb umWeb = new UMWeb(mUrl);
            umWeb.setTitle(mTitle);
            umWeb.setDescription(mDescription);
            if (!TextUtils.isEmpty(mThumb)) {
                umWeb.setThumb(new UMImage(context.getApplicationContext(), mThumb));
            }
            if (mThumbBitmap != null) {
                umWeb.setThumb(new UMImage(context.getApplicationContext(), mThumbBitmap));
            }
            if (mThumbId != -1) {
                umWeb.setThumb(new UMImage(context.getApplicationContext(), mThumbId));
            }
            return new SocializeShareUtils(umWeb, mSocialMedia);
        }
    }
}
