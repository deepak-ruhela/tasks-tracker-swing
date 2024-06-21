package com.model.dao;

import com.model.entity.Project;
import com.util.FileDataDao;
import java.util.Iterator;
import java.util.List;

public class ProjectDao {

//    private List<Project> projects;
//    private TaskDao taskDao;
//    private FileDataDao fileDataDao;
//    private List<Project> projects = FileDataDao.loadProjects();
    public ProjectDao() {
    }

//    public ProjectDao(List<Project> projects, TaskDao taskDao, FileDataDao fileDataDao) {
//        this.projects = projects;
//        this.taskDao = taskDao;
//        this.fileDataDao = fileDataDao;
//    }
    public Project getProjectById(int projectId) {
        List<Project> projects = FileDataDao.loadProjects();
        for (Project project : projects) {
            if (project.getProjectId() == projectId) {
                return project;
            }
        }
        return null;
    }

    public int getProjectIdByProjectName(String name) {
        List<Project> projects = FileDataDao.loadProjects();
        for (Project project : projects) {
            if (project.getProjectName() == null ? name == null : project.getProjectName().equals(name)) {
                return project.getProjectId();
            }
        }
        return 0;
    }

    public List<Project> getAllProjects() {
        return FileDataDao.loadProjects();
    }

    // Method to add a new project
    public void addProject(String projectName) {
        List<Project> projects = FileDataDao.loadProjects();
        Project newProject = new Project();
        newProject.setProjectId(generateProjectId());
        newProject.setProjectName(projectName);
        projects.add(newProject); // Add the new project to the list

        // Update projects in JSON file
        FileDataDao.writeProjectsToFile(projects);
        System.out.println(newProject + " : Project added successfully.");
    }

    public void modifyProjectName(int projectId, String newProjectName) {
        List<Project> projects = FileDataDao.loadProjects();

        boolean taskFound = false;

        // Iterate through projects and update task name if found
        for (Project project : projects) {
            if (project.getProjectId() == projectId) {
                project.setProjectName(newProjectName);
                taskFound = true;
            }
        }

        if (taskFound) {
            // Update projects in JSON file
            FileDataDao.writeProjectsToFile(projects);
            System.out.println("Project with ID " + projectId + " modified successfully.");
        } else {
            System.out.println("Project with ID " + projectId + " not found.");
        }
    }

    // Method to delete a project based on project ID
    public void deleteProject(int projectId) {
        List<Project> projects = FileDataDao.loadProjects();
        Iterator<Project> iterator = projects.iterator();
        boolean projectFound = false;

        while (iterator.hasNext()) {
            Project project = iterator.next();
            if (project.getProjectId() == projectId) {
                iterator.remove(); // Remove the project from the list
                projectFound = true;
                break; // Exit loop once project is found and removed
            }
        }

        if (projectFound) {
            // Update projects in JSON file
            FileDataDao.writeProjectsToFile(projects);
            System.out.println("Project with ID " + projectId + " deleted successfully.");
        } else {
            System.out.println("Project with ID " + projectId + " not found.");
        }
    }

    private int generateProjectId() {
        List<Project> projects = FileDataDao.loadProjects();
        int maxId = 0;
        for (Project project : projects) {
            if (project.getProjectId() > maxId) {
                maxId = project.getProjectId();
            }
        }
        return maxId + 1;
    }
}
