/*
 * Copyright (C) 2019 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.example.chenwh.guidetestapplication;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class CircularView extends View {

    private Paint mPaint;//画笔
    private Map<String, View> mViews;//需要引导的View集合
    private Context mContext;
    private int lineHeight = 0X70;//线条高度
    private int txtLRPadding = 0X80;//文字左右距离
    private int txtTBPadding = 0X30;//文字上下距离

    public CircularView(Context context) {
        super(context);
        init(context);
    }

    public CircularView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircularView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    int mX;//传进来View的横坐标
    int mY;//传进来View的纵坐标
    int height;//传进来的View的高度
    int width;//传进来的View的宽度
    int screenPadding = 0X20;//距屏幕距离

    /**
     * 传入需要引导的View
     *
     * @param view    需要引导的View
     * @param content 引导文字
     */
    public CircularView setView(View view, String content) {
        if (mViews == null) {
            mViews = new HashMap<>();
        }
        mViews.put(content, view);
        return this;
    }

    private void init(Context context) {
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    /**
     * 获取需要展示的引导View信息
     */
    private void initViewOption() {
        View view = getTipView();
        int[] location = new int[2];
        view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        mX = location[0];
        mY = location[1];
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        height = view.getMeasuredHeight();
        width = view.getMeasuredWidth();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        initViewOption();

        int pX = mX + (width / 2);
        int pY = mY + height;

        //画实心圆
        mPaint.setColor(getColor("#449BF3"));
        mPaint.setStyle(Paint.Style.FILL);//实心
        canvas.drawCircle(pX, pY, 14, mPaint);
        canvas.save();

        //画空心圆
        mPaint.setStyle(Paint.Style.STROKE);//空心
        mPaint.setColor(getColor("#AFC8E3"));
        mPaint.setStrokeWidth(10);
        canvas.drawCircle(pX, pY, 14, mPaint);
        canvas.save();

        //画线
        mPaint.setColor(getColor("#449BF3"));
        mPaint.setStrokeWidth(5);
        canvas.drawLine(pX, pY, pX, pY + lineHeight, mPaint);
        pY = pY + lineHeight;
        canvas.save();

        //画矩形
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getColor("#449BF3"));

        Rect rect = new Rect();
        String tipTxt = getTipText();
        mPaint.setTextSize(dip2px(16));
        mPaint.getTextBounds(tipTxt, 0, tipTxt.length(), rect);
        int width = rect.width() + txtLRPadding;//文本的宽度
        int height = rect.height() + txtTBPadding;//文本的高度

        RectF rectF;
        if (pX < screenWidth() / 2) {
            //起始位置在屏幕左侧
            if (pX - (width / 2) >= screenPadding) {
                rectF = new RectF(pX - (width / 2), pY, pX + (width / 2), pY + height);
            } else {
                rectF = new RectF(screenPadding, pY, screenPadding + width, pY + height);
            }
        } else {
            //起始位置在屏幕右侧
            if (pX + width / 2 > screenWidth() - screenPadding) {
                rectF = new RectF(screenWidth() - screenPadding - width, pY, screenWidth() - screenPadding,
                        pY + height);
            } else {
                rectF = new RectF(pX - width / 2, pY, pX + width / 2, pY + height);
            }
        }
        canvas.drawRoundRect(rectF, 10, 10, mPaint);
        canvas.save();

        //画文字
        mPaint.setColor(getColor("#FFFFFF"));
        mPaint.setTextSize(dip2px(16));
        canvas.drawText(tipTxt, rectF.centerX() - ((width - txtLRPadding) / 2),
                rectF.centerY() + ((height - txtTBPadding)
                                           / 2), mPaint);
        canvas.restore();
    }

    /**
     * 获取提示文字
     */
    private String getTipText() {
        Iterator<String> it = mViews.keySet().iterator();
        return it.next();
    }

    /**
     * 获取需要提示的View
     */
    private View getTipView() {
        Iterator<View> itValues = mViews.values().iterator();
        return itValues.next();
    }

    private int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    private int getColor(String color) {
        return Color.parseColor(color);
    }

    public int dip2px(float dip) {
        float density = getScreenDensity();
        return (int) (dip * density + 0.5f);
    }

    public int px2dip(float px) {
        float density = getScreenDensity();
        return (int) (px / density + 0.5f);
    }

    public DisplayMetrics getDisplayMetrics() {
        return mContext.getApplicationContext().getResources().getDisplayMetrics();
    }

    private float getScreenDensity() {
        return getDisplayMetrics().density;
    }

    private int screenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    private int screenHeight() {
        return getDisplayMetrics().heightPixels;
    }
}
