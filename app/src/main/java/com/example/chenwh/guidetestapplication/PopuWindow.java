/*
 * Copyright (C) 2019 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.example.chenwh.guidetestapplication;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class PopuWindow extends PopupWindow {
    private Activity mActivity;

    public PopuWindow(Activity context) {
        super(context);
        this.mActivity = context;
        initView(context);
    }

    TestFram tipFrameView;

    private void initView(Activity context) {
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(false);
        setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setClippingEnabled(false);
        tipFrameView = new TestFram(context);
        setContentView(tipFrameView);
        tipFrameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipFrameView.hasNext()) {
                    tipFrameView.showNext();
                } else {
                    dismiss();
                }
            }
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        tipFrameView.startDraw();
    }

    /**
     * 设置需要提示的View
     * */
    public PopuWindow setView(View view,String content) {
        tipFrameView.setView(view, content);
        return this;
    }
}
