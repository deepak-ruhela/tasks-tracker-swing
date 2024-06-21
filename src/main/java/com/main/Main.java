package com.main;

import com.model.dao.ProjectDao;
import com.model.dao.TaskDao;
import com.model.entity.Project;
import com.model.entity.Task;
import java.util.List;

public class Main {

    public static void main(String[] args) {
//		System.out.println("starting application");
//		 new MyApp();
//		MyApp.main(args);

//        FileDataDao fileDataDao = new FileDataDao();
        TaskDao taskDao = new TaskDao();
        ProjectDao projectDao = new ProjectDao();
//        List<Project> list = projectDao.getAllProjects();
//        projectDao.addProject("pro 45");
//        projectDao.addProject("pro 2");
//        projectDao.modifyProjectName(3, "prd 3.0");
//            projectDao.deleteProject(3);
//        taskDao.addTaskToProject(1, "t simple");
        System.out.println("======= sorting by ID");
        String selectedProject = "pro 2.1";
        System.out.println("Selected Project: " + selectedProject);
        int pId = projectDao.getProjectIdByProjectName(selectedProject);
        Project project = projectDao.getProjectById(pId);
        List<Task> tasks = project.getTasks();
        System.out.println("tasks->"+tasks);
        List<Task> list = taskDao.sortTasksById(tasks);
        System.out.println("list->"+list);
//        loadTasksForSorting(list);
    }
}
