package com.yoy.videoPlayer.utils

import android.content.Context
import com.yoy.v_Base.utils.LogUtils
import java.io.File
import java.io.FileOutputStream

/**
 * Created by Void on 2021/1/29 19:11
 *
 */
object FileUtils {
    /**
     * 将assets资源目录下的文件复制到指定目录
     *
     * @param assetsFileName 资源文件名(包括在assets目录下的完整路径名)
     * @param outputName 输出文件名
     * @param savePath 输出路径
     */
    fun outputAssetsFileToFolder(mContext: Context, assetsFileName: String, outputName: String, savePath: String) :Boolean{
        val mDir = File(savePath)
        // 如果目录不中存在，创建这个目录
        if (!mDir.exists()) mDir.mkdir()
        val mFile = File("$savePath/$outputName")
        try {
            if (mFile.exists()) {
                LogUtils.i(msg = "文件已存在:$savePath/$outputName")
                return true
            }
            val input = mContext.resources.assets.open(assetsFileName)
            val fos = FileOutputStream(mFile)
            val buffer = ByteArray(1024)
            var count = input.read(buffer)
            while (count > 0) {
                fos.write(buffer, 0, count)
                count = input.read(buffer)
            }
            fos.close()
            input.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }
}