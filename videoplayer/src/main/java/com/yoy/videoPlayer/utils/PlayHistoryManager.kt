package com.yoy.videoPlayer.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import android.util.Log
import com.yoy.videoPlayer.beans.VideoFileInfo
import com.yoy.videoPlayer.utils.db.DatabaseHandler
import com.yoy.videoPlayer.utils.db.SQLInfo
import java.util.*
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

/**
 * Created by Void on 2020/12/7 16:56
 * 播放历史记录处理器
 */
object PlayHistoryManager {
    private lateinit var databaseHandler: DatabaseHandler
    private var threadPoolExecutor = ThreadPoolExecutor(
            2, 5, 60,
            TimeUnit.SECONDS, LinkedBlockingDeque()
    )

    fun init(context: Context) {
        val items = HashMap<String, String>()
        items["vName"] = "text"
        items["vPath"] = "text"
        databaseHandler = DatabaseHandler(context,
                SQLInfo("PlayHistory", items),
                1
        )
    }

    fun getDB(): SQLiteDatabase = databaseHandler.writableDatabase

    fun queryData(): LinkedList<VideoFileInfo> {
        val cursor = getDB().query(databaseHandler.sqlInfo.tableName, null,
                null, null, null, null, null)
        val items = LinkedList<VideoFileInfo>()
        if (cursor.count <= 0) return items
        while (cursor.moveToNext()) {
            items.add(VideoFileInfo(
                    cursor.getString(cursor.getColumnIndex("vName")),
                    cursor.getString(cursor.getColumnIndex("vPath"))
            ))
        }
        cursor.close()
        return items
    }

    fun insertData(items: List<VideoFileInfo>) {
        for (i in items) {
            insertData(i)
        }
    }

    fun insertData(item: VideoFileInfo) {
        threadPoolExecutor.execute {
            //忽略已添加的数据
            queryData().let {
                for (q in it) {
                    if (item.vName == q.vName && item.vPath == q.vPath) return@execute
                }
            }
            getDB().insert(databaseHandler.sqlInfo.tableName, null, ContentValues().apply {
                put("vName", item.vName)
                put("vPath", item.vPath)
            })
        }
    }

    fun deleteData(name: String? = null, path: String? = null) {
        threadPoolExecutor.execute {
            if (!TextUtils.isEmpty(name)) {
                getDB().delete(databaseHandler.sqlInfo.tableName, "vName=?", arrayOf(name))
            } else if (!TextUtils.isEmpty(path)) {
                getDB().delete(databaseHandler.sqlInfo.tableName, "vPath=?", arrayOf(path))
            }
        }
    }

    fun deleteAllData() {
        threadPoolExecutor.execute {
            getDB().delete(databaseHandler.sqlInfo.tableName, null, arrayOf(""))
        }
    }
}