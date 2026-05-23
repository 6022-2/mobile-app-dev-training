package com.example.tasklistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tasklistapp.adapter.MemoAdapter;
import com.example.tasklistapp.bean.Memo;
import com.example.tasklistapp.db.MemoDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MemoAdapter memoAdapter;
    private MemoDao memoDao;
    private List<Memo> memoList = new ArrayList<>();
    private String currentUser, currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = getIntent().getStringExtra("user_name");
        currentId = getIntent().getStringExtra("user_id");
        if (currentUser == null) currentUser = "";
        if (currentId == null) currentId = "";

        TextView titleText = findViewById(R.id.title_text);
        titleText.setText("校园代办清单 - " + currentUser + "(" + currentId + ")");

        // 跳转限时待办
        findViewById(R.id.btn_limit_time).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LimitTaskActivity.class);
            intent.putExtra("user_name",currentUser);
            intent.putExtra("user_id",currentId);
            startActivity(intent);
        });

        // 退出登录
        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("user_login", MODE_PRIVATE);
            sp.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        memoDao = new MemoDao(this, currentUser + "_" + currentId);

        memoAdapter = new MemoAdapter(this, memoList, new MemoAdapter.OnItemOperateListener() {
            @Override
            public void setFinish(int pos) {
                if (pos < 0 || pos >= memoList.size()) return;
                Memo memo = memoList.get(pos);
                memo.setStatus(1);
                memoDao.updateMemo(memo);
                runOnUiThread(() -> memoAdapter.notifyItemChanged(pos));
            }

            @Override
            public void setUnFinish(int pos) {
                if (pos < 0 || pos >= memoList.size()) return;
                Memo memo = memoList.get(pos);
                memo.setStatus(0);
                memoDao.updateMemo(memo);
                runOnUiThread(() -> memoAdapter.notifyItemChanged(pos));
            }

            @Override
            public void clickItem(int pos) {
                if (pos < 0 || pos >= memoList.size()) return;
                Memo memo = memoList.get(pos);
                Intent intent = new Intent(MainActivity.this, AddMemoActivity.class);
                intent.putExtra("memo_id", memo.getId());
                intent.putExtra("memo_content", memo.getContent());
                intent.putExtra("user_name", currentUser);
                intent.putExtra("user_id", currentId);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(memoAdapter);

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMemoActivity.class);
            intent.putExtra("user_name", currentUser);
            intent.putExtra("user_id", currentId);
            startActivity(intent);
        });

        refreshData();
    }

    private void refreshData() {
        memoList.clear();
        memoList.addAll(memoDao.getAllMemos());
        memoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }
}