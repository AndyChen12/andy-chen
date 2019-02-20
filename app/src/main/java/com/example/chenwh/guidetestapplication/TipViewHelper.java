/*
 * Copyright (C) 2019 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.example.chenwh.guidetestapplication;

import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class TipViewHelper {
    private Context mContext;
    private PopupWindow popupWindow;
    private TipFrameView tipFrameView;

    public TipViewHelper(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        popupWindow = new PopupWindow();
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setClippingEnabled(false);
        tipFrameView = new TipFrameView(mContext);
        popupWindow.setContentView(tipFrameView);
        tipFrameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipFrameView.hasNext()) {
                    tipFrameView.showNext();
                } else {
                    popupWindow.dismiss();
                }
            }
        });
    }

    /**
     * 添加需要引导的View
     *
     * @param v       需要添加引导的View
     * @param content 引导View展示的内容
     */
    public TipViewHelper addTipView(View v, String content) {
        tipFrameView.setView(v, content);
        mViewMap.put(content,v);
        return this;
    }

    private LinkedHashMap<String, View> mViewMap = new LinkedHashMap<>();//需要引导的View集

    public void showTip() {
        tipFrameView.setView(mViewMap);
        tipFrameView.startDraw();
        popupWindow.showAtLocation(((Activity)mContext).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }
}
