package com.example.tasklistapp.bean;

public class Memo {
    private int id;
    private String content;
    private String time;
    private int status; // 0未完成 1已完成

    public Memo(int id, String content, String time, int status) {
        this.id = id;
        this.content = content;
        this.time = time;
        this.status = status;
    }

    public Memo(String content, String time) {
        this.content = content;
        this.time = time;
        this.status = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}