package com.model.entity;

public class Task {

    private int taskId;

    private String taskName;
    private String description;
    private boolean active;

    public Task() {

    }

    public Task(int taskId, String taskName, String description, boolean active) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.active = active; // Initialize task as active by default
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Task [taskId=" + taskId + ", name=" + taskName + ", description=" + description + ", active=" + active + "]";
    }

}
