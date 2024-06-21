package com.model.entity;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private int projectId;
    private String projectName;
    private List<Task> tasks;

    public Project() {
        tasks = new ArrayList<>();
    }

    public Project(int projectId, String name, List<Task> tasks) {
        super();
        this.projectId = projectId;
        this.projectName = name;
        this.tasks = tasks;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int id) {
        this.projectId = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Project [projectId=" + projectId + ", name=" + projectName + ", tasks=" + tasks + "]";
    }

}
