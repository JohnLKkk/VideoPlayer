package com.example.testdemo.testModel.audioRecorder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.testdemo.R
import com.yoy.v_Base.ui.BaseDefaultActivity
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.ToastUtils
import com.yoy.v_Base.utils.audioplayer.PcmFileUtil
import com.yoy.v_Base.utils.audioplayer.SimpleAudioTrack

class AudioRecorderActivity : BaseDefaultActivity(), InnerAudioRecorder.AudioRecorderListener {
    private val TAG = "Test-AudioRecorder-"
    private var simpleAudioTrack = SimpleAudioTrack()
    private var pcmFileUtil = PcmFileUtil()

    //用于保存测试的音频文件的文件名
    private val audioFileName = "audioRecorderTest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        simpleAudioTrack.init()
        checkPermission()
        setActionBar("音频处理",true)
    }

    override fun getLayoutID(): Int = R.layout.activity_audio_recorder

    override fun isFullScreenWindow(): Boolean = true

    override fun onAudioData(data: ByteArray, start: Int, length: Int) {
        pcmFileUtil.createPcmFile(audioFileName, true)
        pcmFileUtil.write(data, start, length)
    }

    override fun onInitError(message: String) {
    }

    fun onInitRecorder(view: View) {
        InnerAudioRecorder.get().addListener(this)
    }

    fun onStartRecorder(view: View) {
        InnerAudioRecorder.get().startRecorder()
    }

    fun onStopRecorder(view: View) {
        InnerAudioRecorder.get().stopRecorder()
        pcmFileUtil.closeWriteFile()
    }

    fun onDestroyRecorder(view: View) {
        InnerAudioRecorder.get().removeListener(this)
        InnerAudioRecorder.get().kill()
    }

    fun onPlayRecorder(view: View) {
        val file = pcmFileUtil.getFile(audioFileName)
        if (file == null) {
            KLog.e(TAG, "录音测试文件不存在！")
            return
        }
        simpleAudioTrack.playAudio(file)
    }

    fun onStopPlayRecorder(view: View) {
        simpleAudioTrack.release()
    }

    /**
     * 检查权限录音，读写等权限,没有将请求请求权限
     *
     * @return true 取得全部权限
     */
    private fun checkPermission(): Boolean {
        val permissions = arrayOf(
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.SYSTEM_ALERT_WINDOW
        )
        for (a in permissions) {
            if (ContextCompat.checkSelfPermission(this, a) !=
                    PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, a)) {
                    ToastUtils.showShort(this, "已设置拒绝授予权限且不在显示，\n请前往设置手动设置权限")
                } else {
                    ActivityCompat.requestPermissions(this, permissions, 100)
                }
                KLog.d(TAG, "没有读写或录音权限，请求权限")
                return false
            }
        }
        KLog.d(TAG, "已具备基本运行所需权限")
        return true
    }
}