package com.example.tasklistapp.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tasklistapp.R;
import com.example.tasklistapp.bean.Memo;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
    private final Context context;
    private final List<Memo> list;
    private final OnItemOperateListener listener;

    public interface OnItemOperateListener {
        void setFinish(int pos);
        void setUnFinish(int pos);
        void clickItem(int pos);
    }

    public MemoAdapter(Context context, List<Memo> list, OnItemOperateListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_memo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < 0 || position >= list.size()) return; // 防越界
        Memo memo = list.get(position);
        holder.tvContent.setText(memo.getContent());
        holder.tvTime.setText(memo.getTime());

        // 状态样式
        if (memo.getStatus() == 1) {
            holder.tvContent.setTextColor(0xff999999);
            holder.tvContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvContent.setTextColor(0xff222222);
            holder.tvContent.getPaint().setFlags(0);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.clickItem(position);
        });
        holder.btnOk.setOnClickListener(v -> {
            if (listener != null) listener.setFinish(position);
        });
        holder.btnNo.setOnClickListener(v -> {
            if (listener != null) listener.setUnFinish(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent, tvTime;
        Button btnOk, btnNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
            btnOk = itemView.findViewById(R.id.btn_ok);
            btnNo = itemView.findViewById(R.id.btn_no);
        }
    }
}