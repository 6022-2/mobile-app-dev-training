package com.example.tasklistapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 3;

    public DBHelper(Context context, String dbName) {
        super(context, dbName + ".db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 普通待办表
        db.execSQL("CREATE TABLE memo (id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT, time TEXT, status INTEGER)");
        // 限时任务表（强制创建，避免不存在）
        db.execSQL("CREATE TABLE IF NOT EXISTS limittask (id INTEGER PRIMARY KEY AUTOINCREMENT, taskname TEXT, deadlinetime LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE memo ADD COLUMN status INTEGER DEFAULT 0");
        }
        if (oldVersion < 3) {
            db.execSQL("CREATE TABLE IF NOT EXISTS limittask (id INTEGER PRIMARY KEY AUTOINCREMENT, taskname TEXT, deadlinetime LONG)");
        }
    }
}