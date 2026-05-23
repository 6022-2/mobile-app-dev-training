package com.example.tasklistapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tasklistapp.bean.Memo;
import java.util.ArrayList;
import java.util.List;

public class MemoDao {
    private SQLiteDatabase db;

    public MemoDao(Context context, String userKey) {
        DBHelper helper = new DBHelper(context, userKey);
        db = helper.getWritableDatabase();
    }

    public void addMemo(Memo memo) {
        if (memo == null) return;
        ContentValues cv = new ContentValues();
        cv.put("content", memo.getContent());
        cv.put("time", memo.getTime());
        cv.put("status", memo.getStatus());
        db.insert("memo", null, cv);
    }

    public void updateMemo(Memo memo) {
        if (memo == null) return;
        ContentValues cv = new ContentValues();
        cv.put("content", memo.getContent());
        cv.put("time", memo.getTime());
        cv.put("status", memo.getStatus());
        db.update("memo", cv, "id=?", new String[]{String.valueOf(memo.getId())});
    }

    public void deleteMemo(int id) {
        db.delete("memo", "id=?", new String[]{String.valueOf(id)});
    }

    public List<Memo> getAllMemos() {
        List<Memo> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query("memo", null, null, null, null, null, "id DESC");
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String content = cursor.getString(1);
                String time = cursor.getString(2);
                int status = cursor.getInt(3);
                list.add(new Memo(id, content, time, status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }
}