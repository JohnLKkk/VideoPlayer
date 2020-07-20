package com.wyt.videolibrary.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.wyt.videolibrary.JZMediaExo;
import com.wyt.videolibrary.R;
import com.wyt.videolibrary.bean.VideoUrlBean;
import com.wyt.videolibrary.utils.NavigationBarUtil;
import com.wyt.videolibrary.view.VideoJzvd;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static com.wyt.videolibrary.view.VideoJzvd.KEY_VIDEO_NAME_NAME;
import static com.wyt.videolibrary.view.VideoJzvd.KEY_VIDEO_URL_LIST;

/**
 * 单个视频全屏播放
 *
 *                   ArrayList videoUrlBeans = new ArrayList<VideoUrlBean>();
 *                 videoUrlBeans.add(new VideoUrlBean("标准", "http://www.w3school.com.cn/example/html5/mov_bbb.mp4"));
 *                 videoUrlBeans.add(new VideoUrlBean("高清", "http://www.w3school.com.cn/example/html5/mov_bbb.mp4"));
 *                 videoUrlBeans.add(new VideoUrlBean("超清", "http://www.w3school.com.cn/example/html5/mov_bbb.mp4"));
 *                 startActivity(new Intent(MainActivity.this, VideoSingleFullScreenActivity.class)
 *                         .putExtra(VideoJzvd.KEY_VIDEO_URL_LIST, videoUrlBeans)        //视频清晰度集合
 *                         .putExtra(VideoJzvd.KEY_VIDEO_NAME_NAME, "测试"));  //视频名称
 *
 */
public class VideoSingleFullScreenActivity extends AppCompatActivity {

    private VideoJzvd videoJzvd;

//    private AudioManager mAudioManager;
//http://www.w3school.com.cn/example/html5/mov_bbb.mp4

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationBarUtil.hideNavigationBar(this);
        setContentView(R.layout.activity_video_play);
        videoJzvd=findViewById(R.id.videoJzvd);

        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);   //设置视频图像填充模式
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;    //全屏方向为横向
        /**
         * 添加左右上角logo
         */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                videoJzvd.iv_logo_left.setVisibility(View.VISIBLE);
                videoJzvd.iv_logo_right.setVisibility(View.VISIBLE);
                videoJzvd.iv_logo_left.setImageResource(R.drawable.mingshi_left);
                videoJzvd.iv_logo_right.setImageResource(R.drawable.mingshi_right);
            }
        });
        onNewIntent(getIntent());
//        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ArrayList<VideoUrlBean> videoUrlBeans=intent.getParcelableArrayListExtra(KEY_VIDEO_URL_LIST);
        LinkedHashMap map = new LinkedHashMap();
        if (videoUrlBeans==null||videoUrlBeans.size()<1){
            Toast.makeText(this, "获取视频地址失败", Toast.LENGTH_SHORT).show();
            return;
        }
        for (VideoUrlBean video:videoUrlBeans){
            map.put(video.name,video.url);
        }
//        VideoUrlBean videoUrlBean=new VideoUrlBean("aaa","http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
//        map.put("测试","http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        JZDataSource jzDataSource = new JZDataSource(map, getIntent().getStringExtra(KEY_VIDEO_NAME_NAME));
        jzDataSource.currentUrlIndex = 0;
        videoJzvd.setUp(jzDataSource, JzvdStd.SCREEN_FULLSCREEN, JZMediaExo.class);
//        js_video_play.startWindowFullscreen();
        videoJzvd.startButton.performClick();
        videoJzvd.backButton.setOnClickListener(v -> finish());
    }

    //请求焦点
//    public boolean requestFocus() {
//        if(mFocusChangeListener != null ) {
//            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
//                    mAudioManager.requestAudioFocus(mFocusChangeListener,
//                            AudioManager.STREAM_MUSIC,
//                            AudioManager.AUDIOFOCUS_GAIN);
//        }
//        return false;
//    }

    //释放焦点
//    public boolean abandonFocus() {
//        if(mFocusChangeListener != null ) {
//            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager.abandonAudioFocus(mFocusChangeListener);
//        }
//        return false;
//    }


//    public AudioManager.OnAudioFocusChangeListener mFocusChangeListener= new AudioManager.OnAudioFocusChangeListener() {
//        @Override
//        public void onAudioFocusChange(int focusChange) {
//            switch (focusChange) {
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://这说明你临时失去了音频焦点，但是在不久就会再返回来。此时，你必须终止所有的音频播放，但是保留你的播放资源，因为可能不久就会返回来。
//                    break;
//                case AudioManager.AUDIOFOCUS_GAIN://已经获得音频焦点
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://这说明你已经临时失去了音频焦点，但允许你安静的播放音频（低音量），而不是完全的终止音频播放。目前所有的情况下，oFocusChange的时候停止mediaPlayer
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS://已经失去音频焦点很长时间了，必须终止所有的音频播放。因为长时间的失去焦点后，不应该在期望有焦点返回，这是一个尽可能清除不用资源的好位置。例如，应该在此时释放MediaPlayer对象；
//                    //am.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
//                    abandonFocus();
//                    break;
//            }
//        }
//    };

//    public AudioManager.OnAudioFocusChangeListener mFocusChangeListener= new AudioManager.OnAudioFocusChangeListener() {
//        @Override
//        public void onAudioFocusChange(int focusChange) {
//            switch (focusChange) {
//                case AudioManager.AUDIOFOCUS_LOSS:
//                    mAudioManager.abandonAudioFocus(mFocusChangeListener);
//                    break;
//            }
//        }
//    };


    //Home键退出界面暂停播放，返回界面继续播放
    @Override
    protected void onResume() {
        super.onResume();
        //home back

//        mAudioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
//            @Override
//            public void onAudioFocusChange(int focusChange) {
//
//            }
//        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        Jzvd.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //home back
        Jzvd.goOnPlayOnPause();
    }

//    @Override
//    public void onBackPressed() {
//        if (Jzvd.backPress()) {
//            return;
//        }
//        super.onBackPressed();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            videoJzvd.mediaInterface.release();
        }catch (Exception e){

        }

    }
}
