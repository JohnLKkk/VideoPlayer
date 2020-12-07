package com.yoy.videoPlayer.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Void on 2020/12/7 16:59
 * 数据库处理程序
 */
class DatabaseHandler(context: Context, name: String, version: Int) :
        SQLiteOpenHelper(context, name, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }


}