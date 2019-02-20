/*
 * Copyright (C) 2019 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.example.chenwh.guidetestapplication;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * 自定义组合View
 *
 * @author chenwh by 2019/02/18
 */
public class TestFram extends FrameLayout {
    private Context mContext;
    private String mContent;//当前展示的提示文字
    private View mView;//当前需要提示的View
    private TipLineView tipLineView;//线条
    private TipCircularView tipCircularView;//提示点
    private TipContentView tipContentView;//提示文字容器
    private TipLightView tipLightView;//高亮范围
    private TipContentView tipTagView;//提示语
    private float screenWidth;
    private float screenHeight;
    private Map<String, View> mViewMap = new LinkedHashMap<>();//需要引导的View集

    public TestFram(Context context) {
        super(context);
        initView(context);
    }

    public TestFram(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TestFram(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 传入需要引导的View
     *
     * @param view    需要引导的View
     * @param content 引导文字
     */
    public TestFram setView(View view, String content) {
        if (mViewMap == null) {
            mViewMap = new LinkedHashMap<>();
        }
        mViewMap.put(content, view);
        return this;
    }

    /**
     * 传入需要引导的View
     *
     * @param mViewMap    需要引导的View
     */
    public TestFram setView(LinkedHashMap<String, View> mViewMap) {
        if (this.mViewMap == null) {
            this.mViewMap = new LinkedHashMap<>();
        }
        this.mViewMap.putAll(mViewMap);
        return this;
    }



    private void initView(Context context) {
        this.mContext = context;
        tipLineView = new TipLineView(mContext);
        tipCircularView = new TipCircularView(mContext);
        tipLightView = new TipLightView(mContext);
        tipContentView = new TipContentView(mContext);
        tipTagView = new TipContentView(mContext);
        screenHeight = FSScreen.getScreenHeight();
        screenWidth = FSScreen.getScreenWidth();

        tipTagView.setFillColor("#80000000")
                .setTColor("#FFFFFF")
                .setTSize(14f)
                .setRoundRadius(FSScreen.dp2px(5f))
                .setTPadding(FSScreen.dp2px(10f), FSScreen.dp2px(8f), FSScreen.dp2px(10f), FSScreen.dp2px(8f));
    }

    /**
     * 开始绘制布局
     */
    public void startDraw() {
        removeAllViews();
        Iterator<String> keySet = mViewMap.keySet().iterator();
        if (keySet.hasNext()) {
            mContent = keySet.next();
            mView = mViewMap.get(mContent);
            createChildView();
        }
    }

    /**
     * 绘制子View
     */
    private void createChildView() {
        tipLineView.setTipView(mView)
                .setColors("#AFC8E3", "#449BF3")
                .setAuToRun(false)
                .setLineHeight(FSScreen.dp2px(29f));
        tipCircularView.setTipView(mView)
                .setAuToRun(false)
                .setHasAnim(true)
                .setColors("#AFC8E3", "#449BF3")
                .setMaxRadius(16).setSpace(8);
        tipLightView.setTipView(mView)
                .setBlur(FSScreen.dp2px(5f))
                .setRoundRadius(FSScreen.dp2px(5f))
                .setLightType(TipLightView.TipLightType.Circle);
        tipContentView.setTipView(mView)
                .setDistance(FSScreen.dp2px(29f))
                .setFillColor("#319BF5")
                .setTColor("#FFFFFF")
                .setTSize(14f)
                .setRoundRadius(FSScreen.dp2px(5f))
                .setTPadding(FSScreen.dp2px(10f), FSScreen.dp2px(8f), FSScreen.dp2px(10f), FSScreen.dp2px(8f))
                .setTContent(mContent);
        tipTagView.setTContent(mViewMap.size() <= 1 ? "轻触关闭" : "点击继续");
        tipTagView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mView != null) {
                    tipTagView.setX(screenWidth / 2 - tipTagView.getWidth() / 2);
                    if (mView.getY() < screenHeight / 2) {
                        tipTagView.setY(screenHeight - screenHeight / 6);
                    } else {
                        tipTagView.setY(screenHeight / 6);
                    }
                }
            }
        });
        addView(tipLightView,
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(tipLineView,
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(tipCircularView,
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(tipContentView,
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(tipTagView,
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mViewMap.remove(mContent);
        tipCircularView.start();
    }

    public boolean hasNext() {
        return !mViewMap.isEmpty();
    }

    /**
     * 启动下一个
     */
    public void showNext() {
        if (!mViewMap.isEmpty()) {
            removeAllViews();
            startDraw();
        }
    }
}
