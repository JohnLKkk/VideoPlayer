package com.wyt.videolibrary.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;

/**
 * Created by taihong on 2019/11/12
 * 视频View   带右侧播放列表
 */
//public class VideoContainListView extends VideoJzvd implements VideoListAdapter.OnItemClickListener {
//
//    private Context context;
//    private int fixed_orientation = -1;       //固定方向
//    private int videoImageDisplayType = 1;   //视频图像显示类型
//    private int ll_video_list_width;
//    private ConstraintLayout ll_video_list;
//    private TextView video_list;
//    private View bt_close_video_list;
//    private RecyclerView rv_video_list;
//    private VideoListAdapter videoListAdapter;
//    private TranslateAnimation showAnimation, exitAnimation;
//    private OnVideoListItemClickListener onVideoListItemClickListener;
//
//    public interface OnVideoListItemClickListener {
//        public void videoListItemClick(int position, @NotNull VideoListBean.Result data);
//    }
//
//    public void setOnVideoListItemClickListener(OnVideoListItemClickListener onVideoListItemClickListener) {
//        this.onVideoListItemClickListener = onVideoListItemClickListener;
//    }
//
//    private OnOrientationChangedListener onOrientationChangedListener;
//
//    public interface OnOrientationChangedListener {
//        void orientationChanged(int orientation);
//    }
//
//    public void setOnVideoListItemClickListener(OnOrientationChangedListener orientationChanged) {
//        this.onOrientationChangedListener = orientationChanged;
//    }
//
//    public VideoContainListView(Context context) {
//        super(context);
//        this.context = context;
//        init(context, null);
//    }
//
//    public VideoContainListView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.context = context;
//        init(context, attrs);
//    }
//
//    public void init(Context context, AttributeSet attrs) {
//        SAVE_PROGRESS = true;
////        if (attrs != null) {
////            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VideoJzvd);
////            fixed_orientation = typedArray.getInt(R.styleable.VideoJzvd_fixed_orientation, -1);
////            videoImageDisplayType = typedArray.getInt(R.styleable.VideoJzvd_VideoImageDisplayType, Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
////            typedArray.recycle();
////        }
//
//        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);   //设置视频图像填充模式
//        //默认为横屏方向时需要设置退出全屏方向
////        if (getContext().getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
////            NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
////        }
//        rv_video_list = findViewById(R.id.rv_video_list);
//        video_list = findViewById(R.id.video_list);
//        ll_video_list = findViewById(R.id.ll_video_list);
//        bt_close_video_list = findViewById(R.id.bt_close_video_list);
//        video_list.setOnClickListener(this);
//        bt_close_video_list.setOnClickListener(this);
//        titleTextView.setOnClickListener(this);
//
//        ll_video_list.post(() -> {
//            ll_video_list_width = ll_video_list.getWidth();
////            ll_video_list.setVisibility(GONE);
//            ll_video_list.animate().translationX(ll_video_list_width).setDuration(0);
//        });
////        videoListAdapter = new VideoListAdapter();
//        rv_video_list.setLayoutManager(new LinearLayoutManager(context));
//        rv_video_list.addItemDecoration(
//                RItemDecoration.newInstance(
//                        (int) getContext().getResources().getDimension(R.dimen.x1),
//                        (int) getContext().getResources().getDimension(R.dimen.x20), 1, ContextCompat.getColor(getContext(), R.color.transparent)
//                )
//        );
////        rv_video_list.setAdapter(videoListAdapter);
////        videoListAdapter.setOnItemClickListener(this);
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//        ll_video_list.setVisibility(VISIBLE);
//        if (v.getId() == R.id.video_list) {      //点击视频列表按钮
//            ll_video_list.animate().translationX(0).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
//        } else if (v.getId() == R.id.bt_close_video_list) {
//            ll_video_list.animate().translationX(ll_video_list_width).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
//        }
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.layout_video_right_list;
//    }
//
//
//    @Override
//    public void setScreenNormal() {
//        super.setScreenNormal();
//        fullscreenButton.setImageResource(R.drawable.player_btn_fullscreen);
//        fullscreenButton.setVisibility(View.GONE);
//        tinyBackImageView.setVisibility(View.INVISIBLE);
//        changeStartButtonSize((int) getResources().getDimension(R.dimen.jz_start_button_w_h_normal));
//        batteryTimeLayout.setVisibility(View.GONE);
//        video_list.setVisibility(View.GONE);
////        clarity.setVisibility(View.GONE);
//        ll_video_list.setVisibility(VISIBLE);
////        onOrientationChangedListener.orientationChanged(0);
//
//    }
//
//    @Override
//    public void setScreenFullscreen() {
//        super.setScreenFullscreen();
//        fullscreenButton.setImageResource(R.drawable.player_btn_smallscreen);
//        fullscreenButton.setVisibility(View.GONE);
////        backButton.visibility = View.VISIBLE
//        tinyBackImageView.setVisibility(View.INVISIBLE);
//        batteryTimeLayout.setVisibility(View.VISIBLE);
//        video_list.setVisibility(View.VISIBLE);
//
//        changeStartButtonSize((int) getResources().getDimension(R.dimen.jz_start_button_w_h_fullscreen));
//        setSystemTimeAndBattery();
////        onOrientationChangedListener.orientationChanged(1);
//    }
//
//
//    public void setOrientation(){
//        if (getContext().getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
//            NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//        }
//    }
//
//
//    public void setBackViewLocation() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) backButton.getLayoutParams();
//            layoutParams.topMargin = CommonUtils.getStateBarHeight(getContext());
//        }
//    }
//
//    @Override
//    public void itemClick(int position, @NotNull VideoListBean.Result data) {
////        getVideoUrl(false, data.name, data.vkname);
////        onVideoListItemClickListener.videoListItemClick(position, data);
//    }
//
//    public void setVideoDataList(ArrayList<VideoListBean.Result> dataList, VideoListAdapter videoListAdapter) {
//        rv_video_list.setAdapter(videoListAdapter);
//        videoListAdapter.setNewData(dataList);
////        getVideoUrl(true, dataList.get(0).name, dataList.get(0).vkname);
//
//    }
//
//    @Override
//    public void setUp(JZDataSource jzDataSource, int screen, Class mediaInterfaceClass) {
//        super.setUp(jzDataSource, screen, mediaInterfaceClass);
//        if (jzDataSource.urlsMap.size() == 1) {
//            clarity.setVisibility(View.GONE);
//        } else {
//            clarity.setText(jzDataSource.getCurrentKey().toString());
//            clarity.setVisibility(View.VISIBLE);
//        }
//    }
//
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
////        LogTool.e("onTouch：MotionEvent：" + event.getX() + "//" + event.getY() + "  ll_video_list：" + ll_video_list.getX() + "//" + ll_video_list.getY() + "   //" + ll_video_list.getWidth());
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (ll_video_list.getWidth() != v.getMeasuredWidth() && v.getX() < ll_video_list.getX()) {
//                ll_video_list.animate().translationX(ll_video_list_width).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
//            }
//        }
//        return super.onTouch(v, event);
//    }
//
//}
