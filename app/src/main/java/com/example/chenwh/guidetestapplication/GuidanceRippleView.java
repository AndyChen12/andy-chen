/*
 * Copyright (C) 2019 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.example.chenwh.guidetestapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 */
public class GuidanceRippleView extends View {

    private Paint paint;
    private int count;//最大波纹圈数
    private int[] colors;//每一个波纹圈的颜色
    private float space;//两个波纹圈之间距离间隔
    private float speed;
    private boolean autoRun;//是否开启自动播放

    private int frameCountPerSecond = 0;//每秒波纹动画帧数， 默认是48帧。标准的电影动画帧数是24帧
    private float radius = 0;
    private boolean isRunning = false;
    private int clipWidth;
    private int clipHeight;

    public GuidanceRippleView(Context context) {
        super(context);
        initAttr(context, null, 0);
    }

    public GuidanceRippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs, 0);
    }

    public GuidanceRippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
    }

    public void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GuidanceRippleView, defStyleAttr, 0);
        count = a.getInteger(R.styleable.GuidanceRippleView_grvCount, 3);
        space = a.getDimension(R.styleable.GuidanceRippleView_grvSpace, 0);
        frameCountPerSecond = a.getInteger(R.styleable.GuidanceRippleView_grvSpeed, 48);
        autoRun = a.getBoolean(R.styleable.GuidanceRippleView_grvAutoRun, true);
        String colors = a.getString(R.styleable.GuidanceRippleView_grvColors);
        a.recycle();
        if (count < 1)
            throw new IllegalArgumentException("count must be more than one.");
        if (frameCountPerSecond < 1)
            throw new IllegalArgumentException("speed should be one frame per second at least.");
        this.colors = new int[count];
        if (colors != null) {
            colors = colors.trim();
            colors = colors.replace("{", "");
            colors = colors.replace("}", "");
            colors = colors.replace("(", "");
            colors = colors.replace(")", "");
            String[] colorSplit = colors.split(",");
            for (int i = 0; i < count; i++) {
                if (colorSplit.length > 0)
                    this.colors[i] = Color.parseColor(colorSplit[i % colorSplit.length].trim());
                else
                    this.colors[i] = Color.BLUE;
            }
        } else {
            for (int i = 0; i < count; i++) {
                this.colors[i] = Color.BLUE;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        if (space <= 0) {
            space = getMeasuredWidth() * 1.0f / (count * 2);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        speed = -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (speed <= 0) {
            speed = space / frameCountPerSecond;
        }

        if (clipWidth > 0 && clipHeight > 0) {
            int clipLeft = (getWidth() - clipWidth) / 2;
            int clipTop = (getHeight() - clipHeight) / 2;
            canvas.clipRect(clipLeft, clipTop, clipLeft + clipWidth, clipTop + clipHeight);
        }

        float maxRadius = getWidth() / 2.0f;
        int alpha = (int) (0xFF * (1 - radius / maxRadius) + .5f);
        for (int i = 0; i < count; i++) {
            paint.setColor(colors[i]);
            paint.setAlpha(alpha);
            float tempRadius = radius - space * i;
            if (tempRadius > 0)
                canvas.drawCircle(maxRadius, maxRadius, tempRadius, paint);
        }
        radius += speed;
        if (radius > maxRadius)
            radius = 0;
        if (isRunning)
            invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (autoRun)
            start();
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }

    /**
     * 启动动画效果
     * */
    public void start() {
        if (isRunning)
            return;
        radius = 0;
        isRunning = true;
        invalidate();
    }

    /**
     * 停止动画效果
     * */
    public void stop() {
        radius = 0;
        isRunning = false;
        invalidate();
    }

    /**
     * 暂停动画效果
     * */
    public void pause() {
        isRunning = false;
    }

    /**
     * 重启动画效果
     * */
    public void resume() {
        isRunning = true;
        invalidate();
    }

    /**
     * 设置是否自动播放
     *
     * @param autoRun
     */
    public void setAutoRun(boolean autoRun) {
        this.autoRun = autoRun;
    }

    /**
     * 设置波纹动画显示区域
     *
     * @param clipWidth  波纹动画宽
     * @param clipHeight 波纹动画高
     */
    public void setClip(int clipWidth, int clipHeight) {
        this.clipWidth = clipWidth;
        this.clipHeight = clipHeight;
    }

    /**
     * 更新动画效果
     * @param count               最大波纹圈数
     * @param colors              圈纹颜色
     * @param space               波纹间隔
     * @param frameCountPerSecond 波纹帧数
     */
    public void updateAnimation(int count, float space, int frameCountPerSecond, int... colors) {
        if (count < 1)
            throw new IllegalArgumentException("count must be more than one.");
        if (frameCountPerSecond < 1)
            throw new IllegalArgumentException("speed should be one frame per second at least.");

        this.colors = new int[count];
        if (colors == null || colors.length == 0)
            colors = new int[]{Color.BLUE};
        for (int i = 0; i < count; i++) {
            this.colors[i] = colors[i % colors.length];
        }
        this.count = count;
        this.space = space;
        this.speed = -1;

    }
}
