package com.example.tasklistapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.tasklistapp.bean.Course;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {
    private SQLiteDatabase db;

    public CourseDao(Context context, String userKey){
        DBHelper helper = new DBHelper(context, userKey);
        db = helper.getWritableDatabase();
    }

    public void addCourse(Course course){
        ContentValues values = new ContentValues();
        values.put("c_name", course.getCourseName());
        values.put("week_day", course.getWeekDay());
        values.put("section", course.getSection());
        values.put("teacher", course.getTeacher());
        values.put("room", course.getClassroom());
        db.insert("course", null, values);
    }

    public void deleteCourse(int id){
        db.delete("course", "id=?", new String[]{id+""});
    }

    public Course getCourseByPos(int weekDay, int section){
        Course course = null;
        Cursor cursor = db.query("course", null, "week_day=? and section=?",
                new String[]{weekDay+"", section+""}, null, null, null);
        if(cursor.moveToFirst()){
            course = new Course(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
        }
        cursor.close();
        return course;
    }

    public List<Course> getAllCourse(){
        List<Course> list = new ArrayList<>();
        Cursor cursor = db.query("course", null, null, null, null, null, "week_day,section");
        while (cursor.moveToNext()){
            Course c = new Course(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            list.add(c);
        }
        cursor.close();
        return list;
    }
}