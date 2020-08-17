package com.example.testdemo.utlis

import android.content.Context
import android.database.Cursor
import android.net.Uri

/**
 * Created by Void on 2020/8/17 15:32
 *
 */
object FileTools {

    fun getPath(context: Context, uri: Uri): String? {
        if ("content" == uri.scheme) {
            val projection = arrayOf("_data")
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(uri, projection, null, null, null)
                if (cursor == null) return null
                val columnIndex: Int = cursor.getColumnIndexOrThrow("_data")
                return if (cursor.moveToFirst()) {
                    cursor.getString(columnIndex)
                } else
                    null
            } catch (e: Exception) {
            } finally {
                cursor?.close()
            }
        } else if ("file" == uri.scheme) {
            return uri.path
        }
        return null
    }
}