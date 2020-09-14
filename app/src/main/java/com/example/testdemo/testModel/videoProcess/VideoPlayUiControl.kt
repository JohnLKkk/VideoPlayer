package com.example.testdemo.testModel.videoProcess

import android.os.Handler
import android.os.Looper
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.testdemo.R
import com.example.testdemo.utlis.KLog
import com.example.testdemo.utlis.TimeUtils

/**
 * Created by Void on 2020/7/27 17:14
 */
class VideoPlayUiControl(private val mActivity: VideoPlayActivity) : View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {
    private lateinit var mPresenter: VideoPlayPresenter

    //top_layout
    private var backIv: ImageView = mActivity.findViewById(R.id.backIv)
    private var videoNameTv: TextView = mActivity.findViewById(R.id.videoNameTv)
    private var settingIv: ImageView = mActivity.findViewById(R.id.settingIv)

    private var playIv: ImageView = mActivity.findViewById(R.id.playIV)
    private var jumpTime: TextView = mActivity.findViewById(R.id.jumpTime)
    private val videoView: SurfaceView = mActivity.findViewById(R.id.videoView)

    //bottom_layout
    private val currentTime: TextView = mActivity.findViewById(R.id.currentTime)
    private val endTime: TextView = mActivity.findViewById(R.id.endTime)
    private val videoProgressView: SeekBar = mActivity.findViewById(R.id.videoScheduleView)
    private val playBtn: ImageView = mActivity.findViewById(R.id.playBtn)
    private val preBtn: ImageView = mActivity.findViewById(R.id.preBtn)
    private val nextBtn: ImageView = mActivity.findViewById(R.id.nextBtn)
    private val decodeTypeTv: DecodeOptionTextView = mActivity.findViewById(R.id.decodeTypeTv)

    private val mHandler = Handler(Looper.getMainLooper())

    init {
        backIv.setOnClickListener(this)
        settingIv.setOnClickListener(this)

        playIv.setOnClickListener(this)

        playBtn.setOnClickListener(this)
        preBtn.setOnClickListener(this)
        nextBtn.setOnClickListener(this)
        decodeTypeTv.setOnClickListener(this)
        videoProgressView.setOnSeekBarChangeListener(this)
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
        mPresenter.playHandler.setSurfaceHolder(videoView.holder)
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
                }, 2000)
            }
            2 -> {
                currentTime.text = TimeUtils.formatTimeS(tmp)
                val maxTime = mPresenter.playHandler.getMaxTime().toDouble()
                when {
                    position == 0 -> videoProgressView.progress = 0
                    position > maxTime -> videoProgressView.progress = 100
                    else -> {
                        videoProgressView.progress = (position / maxTime * 100).toInt()
//                        KLog.e("maxTime:$maxTime;position:$position;pro:${position / maxTime * 100}")
                    }
                }
            }
            3 -> endTime.text = TimeUtils.formatTimeS(tmp)
            else -> return
        }
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

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar == null||!fromUser) return
        val tmp = seekBar.progress
        if (tmp == 0) return
        val time = mPresenter.playHandler.getMaxTime() * tmp / 100
        setPlayTime(1, time)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        if (seekBar == null) return
        setPlayTime(1, seekBar.progress)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (seekBar == null) return
        mPresenter.goSelectedTime(seekBar.progress)
    }
}