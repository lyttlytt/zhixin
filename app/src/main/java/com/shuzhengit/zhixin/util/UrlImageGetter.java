package com.shuzhengit.zhixin.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.shuzhengit.zhixin.APP;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.view.HtmlTextView;

import java.io.InputStream;
import java.util.UUID;


/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class UrlImageGetter implements Html.ImageGetter {
    //    private Map<String, WeakReference<Drawable>> mCache = Collections.synchronizedMap(new WeakHashMap<String, WeakReference<Drawable>>());
    private TextView mTextView;

    public UrlImageGetter(TextView textView) {
        mTextView = textView;
    }

    @Override
    public Drawable getDrawable(final String source) {


        final HtmlTextView.URLDrawable urlDrawable = new HtmlTextView.URLDrawable();
        //TODO 显示GIF有问题，待完成
//        AppClient.getApiService().getImages(source)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ResponseBody>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody responseBody) {
//                        Drawable drawable = null;
//                        try {
//                            drawable = new GifDrawable(responseBody.bytes());
//                        } catch (IOException e) {
//                            e.printStackTrace();
////                            drawable = new BitmapDrawable();
//                        }
//                        int tvWidth = mTextView.getMeasuredWidth();
//                        InputStream inputStream = responseBody.byteStream();
//                        int[] imageSize = getImageWidthHeight(inputStream);
//                        int width = tvWidth;
//                        int height = tvWidth * imageSize[1] / imageSize[0];
//
//                        drawable.setBounds(0, 0, width, height);
//                        urlDrawable.setBounds(0, 0, width, height);
//                        urlDrawable.setDrawable(drawable);
//                        mTextView.setText(mTextView.getText());
//                    }
//                });
        // TODO: 2017/10/25 加载gif
        if (".gif".endsWith(source)){
            Glide.with(APP.getInstance())
                    .load(source)
                    .asGif()
//                    .asBitmap()
                    .placeholder(R.drawable.loading_img)
                    .centerCrop()
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .signature(new StringSignature(UUID.randomUUID().toString()))

                    .into(new Target<GifDrawable>() {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {

                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
//                            Rect bounds = resource.getBounds();
//                            int tvWidth = mTextView.getMeasuredWidth();
//                            int width = tvWidth;
//                            int height = tvWidth * resource.getHeight() / resource.getWidth();
//                            BitmapDrawable drawable = new BitmapDrawable(null,resource);
//                            drawable.setBounds(0, 0, width, height);
//                            urlDrawable.setBounds(0, 0, width, height);
                            urlDrawable.setDrawable(resource);
                            mTextView.setText(mTextView.getText());
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {

                        }

                        @Override
                        public void getSize(SizeReadyCallback cb) {

                        }

                        @Override
                        public void setRequest(Request request) {

                        }

                        @Override
                        public Request getRequest() {
                            return null;
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onStop() {

                        }

                        @Override
                        public void onDestroy() {

                        }
                    });
        }else {

        Glide.with(APP.getInstance())
                .load(source)
                .asBitmap()
                .placeholder(R.drawable.loading_img)
                .centerCrop()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .signature(new StringSignature(UUID.randomUUID().toString()))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        Rect bounds = placeholder.getBounds();
                        int tvWidth = mTextView.getMeasuredWidth();
                        int width = tvWidth;
                        int height = tvWidth * bounds.bottom / bounds.right;
//                        BitmapDrawable drawable = new BitmapDrawable(null,placeholder);
//                        drawable.setBounds(0, 0, width, height);
                        urlDrawable.setBounds(0, 0, width, height);
                        urlDrawable.setDrawable(placeholder);
                        mTextView.setText(mTextView.getText());
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int tvWidth = mTextView.getMeasuredWidth();
                        int width = tvWidth;
                        int height = tvWidth * resource.getHeight() / resource.getWidth();
                        BitmapDrawable drawable = new BitmapDrawable(null,resource);
                        drawable.setBounds(0, 0, width, height);
                        urlDrawable.setBounds(0, 0, width, height);
                        urlDrawable.setDrawable(drawable);
                        mTextView.setText(mTextView.getText());
                    }
                });
        }

        //暂不支持GIF，之后会添加
//        ImageLoaderUtils.loadImage(source, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                super.onLoadingComplete(imageUri, view, loadedImage);
//                int tvWidth = mTextView.getMeasuredWidth();
//                int width = tvWidth;
//                int height = tvWidth * loadedImage.getHeight() / loadedImage.getWidth();
//                BitmapDrawable drawable = new BitmapDrawable(loadedImage);
//                drawable.setBounds(0, 0, width, height);
//                urlDrawable.setBounds(0, 0, width, height);
//                urlDrawable.setDrawable(drawable);
//                mTextView.setText(mTextView.getText());
//            }
//        });


        return urlDrawable;
    }

    public int[] getImageWidthHeight(InputStream inputStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options); // 此时返回的bitmap为null
        return new int[]{options.outWidth, options.outHeight};
    }

}
