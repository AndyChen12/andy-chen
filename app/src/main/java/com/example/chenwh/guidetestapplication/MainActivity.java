package com.example.chenwh.guidetestapplication;

import java.util.zip.Inflater;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView textView;
    CircularView circularView;
    RelativeLayout mView;
    Activity mActivity;
    TipLineView tipLineView;
    TipCircularView tipCircularView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mView = (RelativeLayout) View.inflate(this, R.layout.activity_main, null);
        setContentView(mView);
        textView = findViewById(R.id.textView);
        circularView = new CircularView(this); //= findViewById(R.id.circularView);
        circularView.setView(textView, "新增功能这是个很长很长的不知道啥玩意啊？你说到底多长呢，这可咋整呢？");

        //        tipLineView = new TipLineView(this);
        //        tipLineView.setTipView(textView)
        //                .setColors("#AFC8E3", "#449BF3")
        //                .setAuToRun(false)
        //                .setSpeed(2f)
        //                .setTipCircularDrawListener(new TipCircularDrawListener() {
        //                    @Override
        //                    public void drawStart() {
        //                    }
        //
        //                    @Override
        //                    public boolean drawEnd() {
        //                        return false;
        //                    }
        //
        //                    @Override
        //                    public void AnimEnd() {
        //                        tipCircularView.start();
        //                    }
        //                });
        //        mView.addView(tipLineView);
        //
        //        tipCircularView = new TipCircularView(this);
        //        tipCircularView.setTipView(textView)
        //                .setAuToRun(false)
        //                .setColors("#AFC8E3", "#449BF3")
        //                .setMaxRadius(15).setSpace(7.5f)
        //                .setTipCircularDrawListener(new TipCircularDrawListener() {
        //                    @Override
        //                    public void drawStart() {
        //                        tipLineView.setVisibility(View.INVISIBLE);
        //                    }
        //
        //                    @Override
        //                    public boolean drawEnd() {
        //                        return false;
        //                    }
        //
        //                    @Override
        //                    public void AnimEnd() {
        //                        tipLineView.start();
        //                        tipLineView.setVisibility(View.VISIBLE);
        //                    }
        //                });
        //        mView.addView(tipCircularView);
        //        tipCircularView.start();

        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                circularView.setView(textView, "新增功能这是个很长很长的不知道啥玩意啊？你说到底多长呢，这可咋整呢？");
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipViewHelper helper = new TipViewHelper(mActivity);
                helper.addTipView(textView, "该扔柔荑花")
                        .addTipView(findViewById(R.id.textView1), "松岛枫威风我")
                        .addTipView(findViewById(R.id.textView2), "是的冯绍是的冯绍峰未分割二狗特工峰")
                        .addTipView(findViewById(R.id.textView3), "非得说的哥的根深水电费舒服舒服蒂固")
                        .addTipView(findViewById(R.id.textView5),"舒服舒服水电费")
                        .addTipView(findViewById(R.id.textView4), "新增功能这是个很长很长的不知道啥玩意啊？你说到底多长呢，这可咋整呢？")
                        .showTip();
            }
        });
    }
}
