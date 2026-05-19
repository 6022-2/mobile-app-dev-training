package com.example.tasklistapp.bean;

public class LimitTask {
    private int id;
    private String taskName;
    private long deadLineTime;

    public LimitTask(int id, String taskName, long deadLineTime) {
        this.id = id;
        this.taskName = taskName;
        this.deadLineTime = deadLineTime;
    }

    public LimitTask(String taskName, long deadLineTime) {
        this.taskName = taskName;
        this.deadLineTime = deadLineTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getDeadLineTime() {
        return deadLineTime;
    }

    public void setDeadLineTime(long deadLineTime) {
        this.deadLineTime = deadLineTime;
    }
}