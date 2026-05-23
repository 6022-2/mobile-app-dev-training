package com.example.tasklistapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tasklistapp.bean.LimitTask;
import java.util.ArrayList;
import java.util.List;

public class LimitTaskDao {
    private SQLiteDatabase db;

    // 按用户独立数据库
    public LimitTaskDao(Context context, String userKey) {
        DBHelper helper = new DBHelper(context, userKey);
        db = helper.getWritableDatabase();
    }

    // 添加限时任务
    public void addLimitTask(LimitTask task) {
        if (task == null) return;
        ContentValues cv = new ContentValues();
        cv.put("taskname", task.getTaskName());
        cv.put("deadlinetime", task.getDeadLineTime());
        db.insert("limittask", null, cv);
    }

    // 删除
    public void deleteLimitTask(int id) {
        db.delete("limittask", "id=?", new String[]{String.valueOf(id)});
    }

    // 查询当前用户所有任务
    public List<LimitTask> getAllLimitTask() {
        List<LimitTask> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query("limittask", null, null, null, null, null, "deadlinetime ASC");
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                long time = cursor.getLong(2);
                list.add(new LimitTask(id, name, time));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }
}