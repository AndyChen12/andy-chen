/*
 * Copyright (C) 2019 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.example.chenwh.guidetestapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;

public class ScreenUtil {
    Context mContext;

    public ScreenUtil(Context mContext) {
        this.mContext = mContext;
    }

    public DisplayMetrics getDisplayMetrics() {
        return mContext.getApplicationContext().getResources().getDisplayMetrics();
    }

    public float getScreenDensity() {
        return getDisplayMetrics().density;
    }

    public int screenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public int screenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public int getColor(int colorId) {
        return mContext.getResources().getColor(colorId);
    }

    public int getColor(String color) {
        return Color.parseColor(color);
    }
}
