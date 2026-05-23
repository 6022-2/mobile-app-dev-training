package com.example.tasklistapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tasklistapp.bean.Course;
import com.example.tasklistapp.db.CourseDao;

public class CourseTableActivity extends AppCompatActivity {
    private LinearLayout llContent;
    private CourseDao courseDao;
    private int clickWeekDay, clickSection;
    private Course selectCourse;
    private static final int MAX_SECTION = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_table);

        String userName = getIntent().getStringExtra("user_name");
        String userId = getIntent().getStringExtra("user_id");
        String userKey = userName + "_" + userId;
        courseDao = new CourseDao(this, userKey);

        llContent = findViewById(R.id.ll_course_content);
        initTableView();
    }

    private void initTableView() {
        llContent.removeAllViews();
        int[] weekArr = {1, 2, 3, 4, 5, 6, 7};

        for (int sec = 1; sec <= MAX_SECTION; sec++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            // 去掉行边距，避免和表头错位
            rowLayout.setPadding(0, 0, 0, 0);

            // -------------------------- 节次列：和左上角“节”字框完全对齐 --------------------------
            TextView sectionTv = new TextView(this);
            // 宽度必须和表头“节”字框完全一致：90dp
            sectionTv.setLayoutParams(new LinearLayout.LayoutParams(90, 120));
            sectionTv.setGravity(android.view.Gravity.CENTER);
            sectionTv.setTextSize(18);
            sectionTv.setBackgroundColor(0xffeeeeee);
            sectionTv.setText(String.valueOf(sec)); // 只显示数字，和表头对齐
            // --------------------------------------------------------------------------------------

            rowLayout.addView(sectionTv);

            for (int week : weekArr) {
                TextView cell = new TextView(this);
                // 课程列宽度由layout_weight平分，和表头的周一~周日列宽完全一致
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 120, 1f);
                // 课程单元格和表头单元格一样，设置1dp的margin，保证列分隔线对齐
                params.setMargins(1,1,1,1);
                cell.setLayoutParams(params);
                cell.setPadding(8, 8, 8, 8);
                cell.setTextSize(16);
                cell.setBackgroundColor(0xffffffff);
                cell.setClickable(true);
                cell.setTag(week + "_" + sec);
                cell.setGravity(android.view.Gravity.CENTER);

                Course course = courseDao.getCourseByPos(week, sec);
                if (course != null) {
                    cell.setText(course.getCourseName());
                    cell.setBackgroundColor(0xffE8F5E9);
                }

                cell.setOnClickListener(v -> {
                    String[] pos = v.getTag().toString().split("_");
                    clickWeekDay = Integer.parseInt(pos[0]);
                    clickSection = Integer.parseInt(pos[1]);
                    selectCourse = courseDao.getCourseByPos(clickWeekDay, clickSection);
                    showEditDialog();
                });
                rowLayout.addView(cell);
            }
            llContent.addView(rowLayout);
        }
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_course, null);
        AlertDialog dialog = builder.create();
        dialog.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.et_course_name);
        EditText etTeacher = dialogView.findViewById(R.id.et_teacher);
        EditText etRoom = dialogView.findViewById(R.id.et_class_room);
        Button btnSave = dialogView.findViewById(R.id.btn_save_course);
        Button btnDel = dialogView.findViewById(R.id.btn_del_course);

        if (selectCourse != null) {
            etName.setText(selectCourse.getCourseName());
            etTeacher.setText(selectCourse.getTeacher());
            etRoom.setText(selectCourse.getClassroom());
        } else {
            etName.setText("");
            etTeacher.setText("");
            etRoom.setText("");
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "请输入课程名称", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectCourse != null) {
                courseDao.deleteCourse(selectCourse.getId());
            }
            Course newCourse = new Course(name, clickWeekDay, clickSection,
                    etTeacher.getText().toString().trim(),
                    etRoom.getText().toString().trim());
            courseDao.addCourse(newCourse);
            Toast.makeText(this, "课程保存成功", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            initTableView();
        });

        btnDel.setOnClickListener(v -> {
            if (selectCourse != null) {
                courseDao.deleteCourse(selectCourse.getId());
                Toast.makeText(this, "课程已删除", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                initTableView();
            } else {
                Toast.makeText(this, "当前位置无课程可删除", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}