package com.yoy.videoPlayer.utils.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Void on 2020/12/7 16:59
 * 数据库处理程序
 */
class DatabaseHandler(context: Context, val sqlInfo: SQLInfo, version: Int) :
        SQLiteOpenHelper(context, sqlInfo.tableName, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(sqlInfo.createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }


}