package com.example.tasklistapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tasklistapp.R;
import com.example.tasklistapp.bean.LimitTask;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class LimitTaskAdapter extends RecyclerView.Adapter<LimitTaskAdapter.TaskHolder> {
    private final Context mContext;
    private final List<LimitTask> mList;
    private final OnTaskClickListener listener;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public interface OnTaskClickListener {
        void onDelete(int pos);
    }

    public LimitTaskAdapter(Context context, List<LimitTask> list, OnTaskClickListener listener) {
        this.mContext = context;
        this.mList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_limit_task, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        if (position < 0 || position >= mList.size()) return;
        LimitTask task = mList.get(position);
        holder.tvName.setText(TextUtils.isEmpty(task.getTaskName()) ? "无名称任务" : task.getTaskName());
        holder.tvEndTime.setText("截止时间：" + sdf.format(task.getDeadLineTime()));

        // 计算剩余时间
        long now = System.currentTimeMillis();
        long diff = task.getDeadLineTime() - now;
        if (diff <= 0) {
            holder.tvRemain.setText("⚠️ 已超时");
            holder.tvRemain.setTextColor(0xfff44336);
        } else {
            long day = diff / (1000 * 60 * 60 * 24);
            long hour = (diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long min = (diff % (1000 * 60 * 60)) / (1000 * 60);
            holder.tvRemain.setText(String.format("剩余：%d天%d时%d分", day, hour, min));
            holder.tvRemain.setTextColor(0xff2196F3);
        }

        holder.btnDel.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(position);
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class TaskHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEndTime, tvRemain;
        Button btnDel;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_task_name);
            tvEndTime = itemView.findViewById(R.id.tv_end_time);
            tvRemain = itemView.findViewById(R.id.tv_remain);
            btnDel = itemView.findViewById(R.id.btn_del_task);
        }
    }
}