/*
 * Copyright (C) 2019 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.example.chenwh.guidetestapplication;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class TipFrameView extends FrameLayout {
    private Context mContext;
    private String mContent;//当前展示的提示文字
    private View mView;//当前需要提示的View
    private TipLineView tipLineView;//线条
    private TipCircularView tipCircularView;//提示点
    private TipContentView tipContentView;//提示文字容器
    private TipLightView tipLightView;//高亮范围
    private TipContentView tipTagView;//提示语
    private Map<String, View> mViewMap = new LinkedHashMap<>();//需要引导的View集
    private ScreenUtil mScreenUtil;

    public TipFrameView(Context context) {
        super(context);
        initView(context);
    }

    public TipFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TipFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 传入需要引导的View
     *
     * @param view    需要引导的View
     * @param content 引导文字
     */
    public TipFrameView setView(View view, String content) {
        if (mViewMap == null) {
            mViewMap = new HashMap<>();
        }
        mViewMap.put(content, view);
        return this;
    }

    public TipFrameView setView(LinkedHashMap linkedHashMap) {
        if (mViewMap == null) {
            mViewMap = new LinkedHashMap<>();
        }
        mViewMap.putAll(linkedHashMap);
        return this;
    }

    private void initView(Context context) {
        this.mContext = context;
        tipLineView = new TipLineView(mContext);
        tipCircularView = new TipCircularView(mContext);
        tipLightView = new TipLightView(mContext);
        tipContentView = new TipContentView(mContext);
        tipTagView = new TipContentView(mContext);
        mScreenUtil = new ScreenUtil(context);
        tipTagView.setFillColor("#80000000")
                .setTColor("#FFFFFF")
                .setTSize(14f)
                .setRoundRadius(5f)
                .setTPadding(20, 10, 20, 10);
        tipTagView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tipTagView.setX(mScreenUtil.screenWidth() / 2 - tipTagView.getWidth() / 2);
                if(mView.getY()< mScreenUtil.screenHeight() / 2){
                    tipTagView.setY(mScreenUtil.screenHeight()-mScreenUtil.screenHeight()/6);
                }else{
                    tipTagView.setY(mScreenUtil.screenHeight()/6);
                }
            }
        });

    }

    /**
     * 开始绘制布局
     */
    public void startDraw() {
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
                .setSpeed(2f)
                .setLineHeight(150f);
        tipCircularView.setTipView(mView)
                .setAuToRun(false)
                .setHasAnim(true)
                .setColors("#AFC8E3", "#449BF3")
                .setMaxRadius(15).setSpace(7.5f);
        tipLightView.setTipView(mView)
                .setBlur(5f)
                .setRoundRadius(5f)
                .setLRPadding(20)
                .setTBPadding(10)
                .setLightType(TipLightView.TipLightType.Circle);
        tipContentView.setTipView(mView)
                .setDistance(150f)
                .setFillColor("#319BF5")
                .setTColor("#FFFFFF")
                .setTSize(14f)
                .setRoundRadius(5f)
                .setTPadding(20, 10, 20, 10)
                .setTContent(mContent);
        tipTagView.setTContent(mViewMap.size()<=1?"轻触关闭":"点击继续");
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

    /**启动下一个*/
    public void showNext() {
        if (!mViewMap.isEmpty()) {
            removeAllViews();
            startDraw();
        }
    }
}
