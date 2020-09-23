package com.example.testdemo.utlis

/**
 * Created by Void on 2020/9/3 17:40
 *
 */
object TimeUtils {
    @JvmStatic
    fun formatTimeS(seconds: Long): String {
        val sb = StringBuffer()
        if (seconds > 3600) {
            val temp = (seconds / 3600).toInt()
            sb.append(if (seconds / 3600 < 10) "0$temp:" else "$temp:")
            changeSeconds(seconds, seconds % 3600 / 60, sb)
        } else {
            changeSeconds(seconds, seconds % 3600 / 60, sb)
        }
        return sb.toString()
    }

    private fun changeSeconds(seconds: Long, temp: Long, sb: StringBuffer): Int {
        sb.append(if (temp < 10) "0$temp:" else "$temp:")
        val tmp: Int = (seconds % 3600 % 60).toInt()
        sb.append(if (tmp < 10) "0$tmp" else "" + tmp)
        return tmp
    }
}