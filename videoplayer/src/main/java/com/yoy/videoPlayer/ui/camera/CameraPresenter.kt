package com.yoy.videoPlayer.ui.camera

import com.yoy.v_Base.ui.BasePresenter
import com.yoy.v_Base.ui.BaseUiControl

/**
 * Created by Void on 2021/2/3 17:58
 *
 */
class CameraPresenter(mActivity: CameraActivity) : BasePresenter(mActivity) {

    override fun getUiControl(): BaseUiControl = getActivityObj().uiControl



    override fun onRelease() {
    }
}