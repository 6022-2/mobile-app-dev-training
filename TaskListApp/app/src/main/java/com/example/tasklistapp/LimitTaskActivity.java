package com.example.tasklistapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tasklistapp.adapter.LimitTaskAdapter;
import com.example.tasklistapp.bean.LimitTask;
import com.example.tasklistapp.db.LimitTaskDao;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LimitTaskActivity extends AppCompatActivity {
    private EditText etTaskName;
    private TextView tvDeadLineShow;
    private RecyclerView rvLimitList;
    private Calendar endCalendar;
    private long selectEndTime = 0;
    private LimitTaskDao taskDao;
    private List<LimitTask> taskList;
    private LimitTaskAdapter taskAdapter;
    private Handler refreshHandler;
    private Runnable refreshRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limit_task);

        // ========== 核心：获取当前登录的 姓名+学号 ==========
        String userName = getIntent().getStringExtra("user_name");
        String userId = getIntent().getStringExtra("user_id");
        String userKey = userName + "_" + userId;

        // ========== 按用户创建独立数据库 ==========
        taskDao = new LimitTaskDao(this, userKey);

        etTaskName = findViewById(R.id.et_task_name);
        tvDeadLineShow = findViewById(R.id.tv_deadline_show);
        Button btnChooseTime = findViewById(R.id.btn_choose_time);
        Button btnAdd = findViewById(R.id.btn_add_task);
        rvLimitList = findViewById(R.id.rv_limit_list);

        endCalendar = Calendar.getInstance();
        taskList = new ArrayList<>();

        // 列表初始化
        rvLimitList.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new LimitTaskAdapter(this, taskList, position -> {
            taskDao.deleteLimitTask(taskList.get(position).getId());
            refreshList();
        });
        rvLimitList.setAdapter(taskAdapter);

        // 选择时间
        btnChooseTime.setOnClickListener(v -> showDateTimePicker());

        // 添加任务
        btnAdd.setOnClickListener(v -> {
            String name = etTaskName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "请输入任务", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectEndTime == 0) {
                Toast.makeText(this, "请选择截止时间", Toast.LENGTH_SHORT).show();
                return;
            }

            // 保存到当前用户数据库
            taskDao.addLimitTask(new LimitTask(name, selectEndTime));

            etTaskName.setText("");
            tvDeadLineShow.setText("未选择截止时间");
            selectEndTime = 0;

            // 刷新当前用户列表
            refreshList();
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        });

        startAutoRefresh();
        refreshList();
    }

    // 刷新当前用户的限时任务
    private void refreshList() {
        taskList.clear();
        taskList.addAll(taskDao.getAllLimitTask());
        taskAdapter.notifyDataSetChanged();
    }

    // 选择时间
    private void showDateTimePicker() {
        Calendar now = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            endCalendar.set(year, month, day);
            new TimePickerDialog(this, (timeView, hour, min) -> {
                endCalendar.set(Calendar.HOUR_OF_DAY, hour);
                endCalendar.set(Calendar.MINUTE, min);
                endCalendar.set(Calendar.SECOND, 0);
                selectEndTime = endCalendar.getTimeInMillis();

                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                        "yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
                tvDeadLineShow.setText("截止：" + sdf.format(selectEndTime));

            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show();
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show();
    }

    // 每秒刷新倒计时
    private void startAutoRefresh() {
        refreshHandler = new Handler(Looper.getMainLooper());
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                taskAdapter.notifyDataSetChanged();
                refreshHandler.postDelayed(this, 1000);
            }
        };
        refreshHandler.post(refreshRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refreshHandler != null && refreshRunnable != null) {
            refreshHandler.removeCallbacks(refreshRunnable);
        }
    }
}