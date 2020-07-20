package com.wyt.videolibrary;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;
import androidx.annotation.MainThread;
import cn.jzvd.JZMediaInterface;
import cn.jzvd.Jzvd;
import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.*;
import com.google.android.exoplayer2.upstream.*;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;

import static cn.jzvd.Jzvd.STATE_AUTO_COMPLETE;
import static cn.jzvd.Jzvd.STATE_NORMAL;

/**
 * Created by th 2019/5/3.
 */
public class JZMediaExo extends JZMediaInterface implements Player.EventListener, VideoListener {
    private SimpleExoPlayer simpleExoPlayer;
    private Runnable callback;
    private String TAG = "JZMediaExo";
    private long previousSeek = 0;

    public JZMediaExo(Jzvd jzvd) {
        super(jzvd);
    }

    @Override
    public void start() {
        if (simpleExoPlayer == null) {
            prepare();
        } else {
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void prepare() {
        Log.e(TAG, "prepare");
        Context context = jzvd.getContext();

        release();
        mMediaHandlerThread = new HandlerThread("JZVD");
        mMediaHandlerThread.start();
        mMediaHandler = new Handler(mMediaHandlerThread.getLooper());//主线程还是非主线程，就在这里
//        mMediaHandler = new Handler(Looper.getMainLooper());//主线程还是非主线程，就在这里
        handler = new Handler();

        mMediaHandler.post(() -> {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE),
                    360000, 600000, 1000, 5000,
                    C.LENGTH_UNSET,
                    false);

            // 2. Create the player

            RenderersFactory renderersFactory = new DefaultRenderersFactory(context);
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector, loadControl);
            // Produces DataSource instances through which media data is loaded.

//            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, ""));
            //            //跨协议重定向
            DefaultBandwidthMeter  mDefaultBandwidthMeter =new DefaultBandwidthMeter();
            DefaultDataSourceFactory dataSourceFactory=new DefaultDataSourceFactory(context, mDefaultBandwidthMeter,
                    new DefaultHttpDataSourceFactory("1",15000,15000,true));

            String currUrl = jzvd.jzDataSource.getCurrentUrl().toString();
            MediaSource videoSource;
            if (currUrl.contains(".m3u8")) {
                videoSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(currUrl), handler, null);
            } else {
                //ExtractorMediaSource
                videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(currUrl));
            }
            simpleExoPlayer.addVideoListener(this);

            Log.e(TAG, "URL Link = " + currUrl);

            simpleExoPlayer.addListener(this);
            Boolean isLoop = jzvd.jzDataSource.looping;
            if (isLoop) {
                simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
            } else {
                simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);
            }
            simpleExoPlayer.prepare(videoSource);
            simpleExoPlayer.setPlayWhenReady(true);
            callback = new onBufferingUpdate();
            if (jzvd.textureView.getSurfaceTexture()!=null){
                 simpleExoPlayer.setVideoSurface(new Surface(jzvd.textureView.getSurfaceTexture()));
            }
//            simpleExoPlayer.setVideoSurface(new Surface(jzvd.textureView.getSurfaceTexture()));
        });

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        handler.post(() -> jzvd.onVideoSizeChanged(width, height));
    }

    @Override
    public void onRenderedFirstFrame() {
        Log.e(TAG, "onRenderedFirstFrame");
    }

    @Override
    public void pause() {
        if (simpleExoPlayer!=null)
        simpleExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public boolean isPlaying() {
        if (simpleExoPlayer!=null){
          return   simpleExoPlayer.getPlayWhenReady();
        }else {
            return false;
        }
//        return simpleExoPlayer.getPlayWhenReady();
    }

    @Override
    public void seekTo(long time) {
        if (simpleExoPlayer!=null&&time != previousSeek) {
            simpleExoPlayer.seekTo(time);
            previousSeek = time;
            jzvd.seekToInAdvance = time;
        }
    }

    @Override
    public void release() {
        if (mMediaHandler != null && mMediaHandlerThread != null && simpleExoPlayer != null) {//不知道有没有妖孽
            HandlerThread tmpHandlerThread = mMediaHandlerThread;
            SimpleExoPlayer tmpMediaPlayer = simpleExoPlayer;
            JZMediaInterface.SAVED_SURFACE = null;
            mMediaHandler.post(() -> {
                tmpMediaPlayer.release();//release就不能放到主线程里，界面会卡顿
                tmpHandlerThread.quit();
            });
            simpleExoPlayer = null;
        }
    }

    @Override
    public long getCurrentPosition() {
        if (simpleExoPlayer != null)
            return simpleExoPlayer.getCurrentPosition();
        else return 0;
    }

    @Override
    public long getDuration() {
        if (simpleExoPlayer != null)
            return simpleExoPlayer.getDuration();
        else return 0;
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        if (simpleExoPlayer!=null)
        simpleExoPlayer.setVolume(leftVolume);
        simpleExoPlayer.setVolume(rightVolume);
    }

    @Override
    public void setSpeed(float speed) {
        PlaybackParameters playbackParameters = new PlaybackParameters(speed, 1.0F);
        if (simpleExoPlayer!=null)
        simpleExoPlayer.setPlaybackParameters(playbackParameters);
    }

    @Override
    public void onTimelineChanged(final Timeline timeline, Object manifest, final int reason) {
        Log.e(TAG, "onTimelineChanged");
//        JZMediaPlayer.instance().mainThreadHandler.post(() -> {
//                if (reason == 0) {
//
//                    JzvdMgr.getCurrentJzvd().onInfo(reason, timeline.getPeriodCount());
//                }
//        });
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.e(TAG, "onLoadingChanged");
    }

    @Override
    public void onPlayerStateChanged(final boolean playWhenReady, final int playbackState) {
        Log.e(TAG, "onPlayerStateChanged" + playbackState + "/ready=" + String.valueOf(playWhenReady));
        handler.post(() -> {
            switch (playbackState) {
                case Player.STATE_IDLE: {
                }
                break;
                case Player.STATE_BUFFERING: {
                    jzvd.onStatePreparing();        //缓冲中显示加载框  （自定义增加）
                    handler.post(callback);
                }
                break;
                case Player.STATE_READY: {
                    if (playWhenReady) {
                        jzvd.onStatePlaying();
                    } else {
                    }
                }
                break;
                case Player.STATE_ENDED: {
                    jzvd.onAutoCompletion();
                }
                break;
            }
        });
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        handler.post(() -> jzvd.onError(1000, 1000));
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {
        handler.post(() -> jzvd.onSeekComplete());
    }

    @Override
    public void setSurface(Surface surface) {
        if (surface!=null) {
            simpleExoPlayer.setVideoSurface(surface);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (jzvd.state== STATE_AUTO_COMPLETE||jzvd.state== STATE_NORMAL){
            return;
        }
        if (SAVED_SURFACE == null) {
            SAVED_SURFACE = surface;
            prepare();
        } else {
            jzvd.textureView.setSurfaceTexture(SAVED_SURFACE);
//            simpleExoPlayer.setVideoSurface(new Surface(SAVED_SURFACE));
        }
//        JZMediaInterface.SAVED_SURFACE = surface;
//        prepare();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private class onBufferingUpdate implements Runnable {
        @Override
        public void run() {
            if (simpleExoPlayer != null) {
                final int percent = simpleExoPlayer.getBufferedPercentage();
                handler.post(() -> jzvd.setBufferProgress(percent));
                if (percent < 100) {
                    handler.postDelayed(callback, 300);
                } else {
                    handler.removeCallbacks(callback);
                }
            }
        }
    }
}
