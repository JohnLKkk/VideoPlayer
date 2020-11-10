package com.example.testdemo.testModel.audioRecorder

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import com.yoy.v_Base.utils.AppCode
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.audioplayer.PcmFileUtil
import java.util.*

/**
 * Created by Void on 2020/4/20 15:35
 * 内置音频录制器
 */
class InnerAudioRecorder {
    companion object {
        private var instance: InnerAudioRecorder? = null
            get() {
                if (field == null) field = InnerAudioRecorder()
                return field
            }

        fun get(): InnerAudioRecorder = instance!!
    }

    //缓存获取的采样率  32000、16000、8000
    private val bufferSampleRate = 16000

    //缓冲区大小
    private val bufferSize: Int = AudioRecord.getMinBufferSize(
            bufferSampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
    )

    //音频录制器
    private var audioRecorder: AudioRecord? = null
    private val isDebug = false
    private var audioThread: AudioRecorderThread? = null
    private val listeners = LinkedList<AudioRecorderListener>()
    private val pcmFileUtil = PcmFileUtil()

    //尝试重新获取麦克风的次数，当成功获取到麦克风或停止录音时，应当对这个值归0
    private var tryReInitNum = 0

    init {
        audioThread = AudioRecorderThread()
        pcmFileUtil.createPcmFile("InnerAudioRecorder_output_pcm", true)
        createRecorder()
    }

    fun addListener(listener: AudioRecorderListener) {
        if (listeners.contains(listener)) return//防止重复添加
        listeners.add(listener)
    }

    fun removeListener(listener: AudioRecorderListener) {
        if (listeners.contains(listener)) listeners.remove(listener)
    }

    fun startRecorder() {
        createRecorder()
        if (audioRecorder?.state == AudioRecord.STATE_INITIALIZED) {
            audioRecorder?.startRecording()
        }
        try {
            audioThread?.interrupt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        audioThread = AudioRecorderThread()
        audioThread?.isKeepRunning = true
        audioThread?.start()
    }

    fun stopRecorder() {
        audioThread?.isKeepRunning = false
        tryReInitNum = 0
        try {
            if (audioRecorder?.state == AudioRecord.STATE_INITIALIZED) {
                audioRecorder?.stop()
            }
            audioRecorder?.release()
        } catch (e: Exception) {

        }
        audioRecorder = null
    }

    /**
     * 结束录音
     */
    fun kill() {
        audioThread?.interrupt()
        tryReInitNum = 0
        instance = null
    }

    /**
     * @return Boolean  当前是否正在录制
     */
    fun isRecording(): Boolean = audioThread?.isKeepRunning ?: false

    private fun outLog(string: String) {
        if (isDebug) KLog.d("系统音频录制器-$string")
    }

    /**
     * 尝试重新获取麦克风
     */
    @Synchronized
    private fun tryReinitRecorder() {
        if (tryReInitNum > AppCode.tryGetRecorderFailMaxCount) {
            tryReInitNum = 0
            kill()
            return
        }
        tryReInitNum++
        try {
            Thread.sleep(1000L)
            outLog("Error### ReInit audioRecorder")
            audioRecorder?.stop()
            audioRecorder?.release()
        } catch (e: Exception) {
        }
        createRecorder()
        Thread.sleep(1000L)
        startRecorder()
    }

    private fun createRecorder() {
        if (audioRecorder != null) return
        audioRecorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                bufferSampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
        )
    }

    interface AudioRecorderListener {
        fun onAudioData(data: ByteArray, start: Int, length: Int)
        fun onInitError(message: String)
    }

    inner class AudioRecorderThread : Thread() {
        var isKeepRunning: Boolean = true

        override fun run() {
            super.run()
            //设置线程运行的优先级
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
            //缓冲长度声明
            var readLength: Int
            //用于显示是否正在运行Log的时间标记
            var logTimeMark = 0L
            //线程标记
            val threadTag: Long = Random(System.currentTimeMillis()).nextLong()
            //缓冲区创建
            val buffer = ByteArray(bufferSize)
            //如果当前录制器状态不可用，则停止
            if (audioRecorder?.state != AudioRecord.STATE_INITIALIZED) {
                for (l in listeners.iterator()) {
                    l.onInitError("系统音频录制器初始化失败")
                }
                outLog("初始化失败")
                //尝试重新获取麦克风
                tryReinitRecorder()
                return
            }
            try {
                if (audioRecorder?.state == AudioRecord.STATE_INITIALIZED) audioRecorder?.startRecording()
                while (isKeepRunning && audioRecorder?.state == AudioRecord.STATE_INITIALIZED) {
                    readLength = audioRecorder?.read(buffer, 0, bufferSize) ?: -1
                    //打印运行日志
                    if (System.currentTimeMillis() - logTimeMark > 5000L) {
                        outLog("Recorder thread is running\n" +
                                "Pid:${Process.myPid()};\n" +
                                "Tid:${Process.myTid()};\n" +
                                "Tag:$threadTag")
                        logTimeMark = System.currentTimeMillis()
                    }
                    if (readLength == AudioRecord.ERROR_INVALID_OPERATION ||
                            readLength == AudioRecord.ERROR_BAD_VALUE ||
                            readLength == AudioRecord.ERROR) {
                        outLog("Error### System AudioRecord ERROR:$readLength")
                        tryReinitRecorder()
                        continue
                    }
                    tryReInitNum = 0
                    if (readLength > 0) {
                        for (l in listeners.iterator()) {
                            l.onAudioData(buffer, 0, readLength)
                        }
                        pcmFileUtil.write(buffer)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                tryReInitNum = 0
            }
            //由于异常情况才会走到这个方法
            if (isKeepRunning) stopRecorder()
        }
    }
}