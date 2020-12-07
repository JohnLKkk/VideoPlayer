package com.yoy.videoPlayer.utils.db

import java.lang.StringBuilder

/**
 * Created by Void on 2020/12/7 17:57
 * 表信息
 */
class SQLInfo(val tableName: String, val items: Map<String, String>) {

    val createTable: String
        get() {
            return StringBuilder().apply {
                append("create table ")
                append(tableName)
                append(" ( id integer primary key autoincrement,")
                for (i in items.entries) {
                    append(i.key)
                    append(" ")
                    append(i.value)
                    append(",")
                }
                delete(length - 2, length-1)
                append(")")
            }.toString()
        }

}