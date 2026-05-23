package com.example.tasklistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etName, etStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 自动登录
        SharedPreferences sp = getSharedPreferences("user_login", MODE_PRIVATE);
        String lastUser = sp.getString("last_user", "");
        String lastId = sp.getString("last_id", "");
        if (!lastUser.isEmpty() && !lastId.isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("user_name", lastUser);
            intent.putExtra("user_id", lastId);
            startActivity(intent);
            finish();
        }

        etName = findViewById(R.id.et_name);
        etStudentId = findViewById(R.id.et_student_id);
        Button btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String studentId = etStudentId.getText().toString().trim();

            if (name.isEmpty() || studentId.isEmpty()) {
                Toast.makeText(LoginActivity.this, "姓名和学号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            // 保存登录信息
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("last_user", name);
            editor.putString("last_id", studentId);
            editor.apply();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("user_name", name);
            intent.putExtra("user_id", studentId);
            startActivity(intent);
            finish();
        });
    }
}