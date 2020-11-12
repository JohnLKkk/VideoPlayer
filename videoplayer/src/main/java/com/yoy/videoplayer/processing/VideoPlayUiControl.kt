package com.yoy.videoplayer.processing

import android.os.Handler
import android.os.Looper
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.yoy.v_Base.utils.TimeUtils
import com.yoy.videoplayer.R

/**
 * Created by Void on 2020/7/27 17:14
 */
class VideoPlayUiControl(private val mActivity: VideoPlayActivity) :
        View.OnClickListener,
        VideoPreviewBar.ProgressCallback {
    private lateinit var mPresenter: VideoPlayPresenter

    //top_layout
    private var backIv: ImageView = mActivity.findViewById(R.id.backIv)
    private var videoNameTv: TextView = mActivity.findViewById(R.id.videoNameTv)
    private var settingIv: ImageView = mActivity.findViewById(R.id.settingIv)

    private var playIv: ImageView = mActivity.findViewById(R.id.playIV)
    private var jumpTime: TextView = mActivity.findViewById(R.id.jumpTime)
    private val videoView: SurfaceView = mActivity.findViewById(R.id.videoView)

    //bottom_layout
    private val playTime: TextView = mActivity.findViewById(R.id.playTime)
    private val videoPreview: VideoPreviewBar = mActivity.findViewById(R.id.videoPreview)
    private val playBtn: ImageView = mActivity.findViewById(R.id.playBtn)
    private val preBtn: ImageView = mActivity.findViewById(R.id.preBtn)
    private val nextBtn: ImageView = mActivity.findViewById(R.id.nextBtn)
    private var decodeTypeTv: DecodeOptionTextView = mActivity.findViewById(R.id.decodeTypeTv)

    private val mHandler = Handler(Looper.getMainLooper())

    private var currentTime = ""
    private var endTime = ""

    init {
        backIv.setOnClickListener(this)
        settingIv.setOnClickListener(this)

        playIv.setOnClickListener(this)

        playBtn.setOnClickListener(this)
        preBtn.setOnClickListener(this)
        nextBtn.setOnClickListener(this)
        videoPreview.callback = this
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backIv -> mActivity.finish()
            R.id.settingIv,
            R.id.playIV -> mPresenter.onClickPlay()
            R.id.playBtn -> changePlayState()
            R.id.preBtn -> mPresenter.preVideo()
            R.id.nextBtn -> mPresenter.nextVideo()
        }
    }

    fun setPresenter(presenter: VideoPlayPresenter) {
        this.mPresenter = presenter
        decodeTypeTv.setClickCallback(mPresenter)
        decodeTypeTv.setTextAndDecodeType(DecodeType.FFMPEGDecoder)
        videoView.holder.addCallback(mPresenter.playHandler)
    }

    fun setVideoName(name: String) {
        videoNameTv.text = name
    }

    fun onStartPlay() {
        playIv.visibility = View.GONE
    }

    /**
     * 设置时间的显示
     * @param type 类型:
     * 1设置跳转的显示时间 jumpTime
     * 2设置当前播放的时间进度 currentTime
     * 3设置结束播放的时间进度 endTime
     * @param position 时间，单位 ms
     */
    fun setPlayTime(type: Int, position: Int) {
        val tmp = position / 1000L
        when (type) {
            1 -> {
                mHandler.removeCallbacksAndMessages(null)
                jumpTime.visibility = View.VISIBLE
                jumpTime.text = TimeUtils.formatTimeS(tmp)
                //一段时间后隐藏
                mHandler.postDelayed({
                    jumpTime.visibility = View.GONE
                }, 1000)
                return
            }
            2 -> {
                if (jumpTime.visibility != View.GONE) return
                currentTime = TimeUtils.formatTimeS(tmp)
                val maxTime = mPresenter.playHandler.getMaxTime().toDouble()
                when {
                    position == 0 -> videoPreview.setProgress(0)
                    position > maxTime -> videoPreview.setProgress(100)
                    else -> {
                        videoPreview.setProgress((position / maxTime * 100).toInt())
//                        KLog.e("maxTime:$maxTime;position:$position;pro:${position / maxTime * 100}")
                    }
                }
            }
            3 -> endTime = TimeUtils.formatTimeS(tmp)
            else -> return
        }
        playTime.text = mActivity.getString(R.string.playTime, currentTime, endTime)
    }

    fun onRelease() {
    }

    private fun changePlayState() {
        if (mPresenter.playHandler.isPlaying()) {
            playBtn.background = ContextCompat.getDrawable(mActivity, R.drawable.ic_stop_play)
            mPresenter.playHandler.pause()
        } else {
            playBtn.background = ContextCompat.getDrawable(mActivity, R.drawable.ic_start_play)
            mPresenter.playHandler.start()
        }
    }

    override fun onChangeProgress(index: Int, fromUser: Boolean) {
        setPlayTime(1, mPresenter.playHandler.getMaxTime() * index / 100)
    }

    override fun onTouchCallback(type: Int, index: Int) {
        if (type == 0) {
            setPlayTime(1, index)
        } else {
            mPresenter.goSelectedTime(index)
        }
    }
}