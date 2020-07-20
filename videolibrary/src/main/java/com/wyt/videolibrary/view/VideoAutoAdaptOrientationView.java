package com.wyt.videolibrary.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.wyt.videolibrary.R;
import com.wyt.videolibrary.utils.CommonUtils;

import cn.jzvd.Jzvd;

/**
 * 自适应方向旋转全屏
 */
public class VideoAutoAdaptOrientationView extends VideoJzvd {

    private int VideoImageDisplayType;

    public VideoAutoAdaptOrientationView(Context context) {
        super(context);
        init();
    }

    public VideoAutoAdaptOrientationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
//        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);   //设置视频图像填充模式
//        Jzvd.clearSavedProgress(getContext(), null);
//        SAVE_PROGRESS = false;
        //默认为横屏方向时需要设置退出全屏方向
    //    NORMAL_ORIENTATION = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();  //设置当前方向，不设置全屏返回方向不正确  （默认为纵向 ）
    }


    public void setOrientation(int orientation){
//        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR
//        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        Jzvd.FULLSCREEN_ORIENTATION =orientation;
        Jzvd.NORMAL_ORIENTATION =orientation;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_phone_video;
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        fullscreenButton.setImageResource(R.drawable.player_btn_fullscreen);
//        backButton.visibility = View.GONE
        tinyBackImageView.setVisibility(View.INVISIBLE);
        changeStartButtonSize((int) getResources().getDimension(R.dimen.jz_start_button_w_h_normal));
        batteryTimeLayout.setVisibility(View.GONE);
        clarity.setVisibility(View.GONE);
        backButton.setVisibility(INVISIBLE);
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);   //设置视频图像填充模式
//        Jzvd.setVideoImageDisplayType(VideoImageDisplayType);
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        fullscreenButton.setImageResource(R.drawable.player_btn_smallscreen);
//        backButton.visibility = View.VISIBLE
        tinyBackImageView.setVisibility(View.INVISIBLE);
        batteryTimeLayout.setVisibility(View.VISIBLE);
        backButton.setVisibility(VISIBLE);
        if (jzDataSource.urlsMap.size() == 1) {
            clarity.setVisibility(View.GONE);
        } else {
            clarity.setText(jzDataSource.getCurrentKey().toString());
            clarity.setVisibility(View.VISIBLE);
        }
        changeStartButtonSize((int) getResources().getDimension(R.dimen.jz_start_button_w_h_fullscreen));
        setSystemTimeAndBattery();
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);   //设置视频图像填充模式
//        setVideoImageDisplayType();
    }


    public void  setBackViewLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) backButton.getLayoutParams();
            layoutParams.topMargin = CommonUtils.getStateBarHeight(getContext());
        }
    }


//    public void setVideoImageDisplayType(){
//        int  orientation = getResources().getConfiguration().orientation;
//        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);   //设置视频图像填充模式
//        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);   //设置视频图像填充模式
//        }
//    }
}
