package com.shuzhengit.zhixin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.util.ResourceUtils;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2018/1/29 14:03
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class SignInView extends View {
    private static final String TAG = "CircleView";
    private static final int DEFAULT_SIZE = 200;
    private   int mRingWidth;
    private int width ;
    private int height;
    private Paint mRingPaint,mInnerCircle;
    private int mRingColor;
    private int mInnerColor;
    private Paint mTextPaint;
    private  int mFontSize ;
    private String mText = "";
    private int mTextColor ;
    public SignInView(Context context) {
        this(context, null);
    }

    public SignInView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignInView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SignInView);
        mRingColor = a.getColor(R.styleable.SignInView_ringColor, ResourceUtils.getResourceColor(context, R.color
                .colorTransparent));
        mInnerColor = a.getColor(R.styleable.SignInView_innerColor, ResourceUtils.getResourceColor(context, R.color
                .colorTransparent));
        mRingWidth = (int) a.getDimension(R.styleable.SignInView_ringWidth,20);
        mFontSize = (int) a.getDimension(R.styleable.SignInView_fontSize,20);
        mText = a.getString(R.styleable.SignInView_text);
        mTextColor = a.getColor(R.styleable.SignInView_textColor,Color.WHITE);
        if (TextUtils.isEmpty(mText)){
            mText = " ";
//            mText = new SimpleDateFormat("hh:mm:ss").format(System.currentTimeMillis());
        }
        a.recycle();

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mRingWidth);
        mRingPaint.setColor(mRingColor);

        mInnerCircle = new Paint();
        mInnerCircle.setAntiAlias(true);
        mInnerCircle.setStyle(Paint.Style.FILL);
        mInnerCircle.setColor(mInnerColor);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mFontSize);
        mTextPaint.setColor(mTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int width =0;
        int height =0;
        switch (widthSpecMode) {
            case MeasureSpec.EXACTLY:
                width = widthSpecSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(DEFAULT_SIZE,widthSpecSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                width = DEFAULT_SIZE;
                break;
            default:
                break;
        }
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (heightSpecMode) {
            case MeasureSpec.EXACTLY:
                height = heightSpecSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(DEFAULT_SIZE,heightSpecSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                height = DEFAULT_SIZE;
                break;
            default:break;
        }
//        LogUtils.d(TAG,"measure width : " + width + "   height : " + height);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int minSize = Math.min(width, height);

        canvas.drawCircle(width/2,height/2,minSize/2-mRingWidth,mInnerCircle);
        canvas.drawCircle(width/2,height/2,minSize/2-mRingWidth,mRingPaint);
        float v = mTextPaint.measureText(mText);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
//        Log.d(TAG,"fontMetrics top : " + fontMetrics.top);
//        Log.d(TAG,"fontMetrics leading : " + fontMetrics.leading);
//        Log.d(TAG,"fontMetrics ascent : " + fontMetrics.ascent);
//        Log.d(TAG,"fontMetrics descent : " + fontMetrics.descent);
//        Log.d(TAG,"fontMetrics bottom : " + fontMetrics.bottom);
        float y = height / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
//        Log.d(TAG,"base line : " + y);
        canvas.drawText(mText,0,mText.length(), (width-v)/2 ,y,mTextPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    public void setText(String text) {
        mText = text;
        invalidate();
    }

    public void setFontSize(int fontSize) {
        mFontSize = fontSize;
        mTextPaint.setTextSize(mFontSize);
        invalidate();
    }
}
