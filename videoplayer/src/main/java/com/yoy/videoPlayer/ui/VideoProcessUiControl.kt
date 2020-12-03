package com.yoy.videoPlayer.ui

import android.view.SurfaceView
import android.widget.FrameLayout
import com.yoy.videoPlayer.R

/**
 * Created by Void on 2020/12/3 17:08
 *
 */
class VideoProcessUiControl(mActivity: VideoProcessActivity){
    private var videoView: SurfaceView = mActivity.findViewById(R.id.videoView)
    private var defaultControlView: FrameLayout = mActivity.findViewById(R.id.defaultControlView)
    private var functionImportLayout: FrameLayout = mActivity.findViewById(R.id.functionImportLayout)
    init {

    }
}