package com.example.tasklistapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tasklistapp.bean.Memo;
import com.example.tasklistapp.db.MemoDao;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddMemoActivity extends AppCompatActivity {
    private EditText etContent;
    private MemoDao memoDao;
    private int memoId = -1;
    private String userName, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        userName = getIntent().getStringExtra("user_name");
        userId = getIntent().getStringExtra("user_id");
        memoDao = new MemoDao(this, userName + "_" + userId);

        etContent = findViewById(R.id.et_content);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnDelete = findViewById(R.id.btn_delete);

        memoId = getIntent().getIntExtra("memo_id", -1);
        if (memoId != -1) {
            String content = getIntent().getStringExtra("memo_content");
            etContent.setText(content);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> {
            String content = etContent.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(AddMemoActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
            if (memoId == -1) {
                memoDao.addMemo(new Memo(content, time));
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            } else {
                // 编辑保留原有状态
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            memoDao.deleteMemo(memoId);
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // 返回键弹窗询问
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("确定要返回吗？未保存内容将会丢失")
                    .setPositiveButton("确定", (dialog, which) -> finish())
                    .setNegativeButton("取消", null)
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}