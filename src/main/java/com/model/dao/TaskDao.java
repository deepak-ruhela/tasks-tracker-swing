package com.model.dao;

import com.model.entity.Project;
import com.model.entity.Task;
import com.util.FileDataDao;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class TaskDao {

//    private List<Project> projects = FileDataDao.loadProjects();
    public TaskDao() {
    }

    public List<Task> getTasksForProject(int projectId) {
        List<Project> projects = FileDataDao.loadProjects();
        List<Task> tasks = new ArrayList<>();

        // Find the project with the specified project ID
        for (Project project : projects) {
            if (project.getProjectId() == projectId) {
                tasks.addAll(project.getTasks());
                break; // Exit loop once project is found
            }
        }

        return tasks;
    }

    // Method to show all tasks across all projects
    public List<Task> getAllTasks() {
        List<Project> projects = FileDataDao.loadProjects();
        List<Task> allTasks = new ArrayList<>();

        // Iterate through each project and collect tasks
        for (Project project : projects) {
            allTasks.addAll(project.getTasks());
        }

        return allTasks;
    }

    public Task getTaskByTaskId(int taskId) {
        List<Project> projects = FileDataDao.loadProjects();
        // Iterate through projects and update task name if found
        for (Project project : projects) {
            List<Task> tasks = project.getTasks();

            for (Task task : tasks) {
                if (task.getTaskId() == taskId) {
                    return task;
                }
            }
        }

        return null;
    }

    public int getTaskIdByTaskName(String taskName) {
        List<Project> projects = FileDataDao.loadProjects();
        for (Project project : projects) {
            List<Task> tasks = project.getTasks();

            for (Task task : tasks) {
                if (task.getTaskName() == null ? taskName == null : task.getTaskName().equals(taskName)) {
                    return task.getTaskId();
                }
            }
        }

        return 0;
    }

    public void addTaskToProject(int projectId, String taskName) {
        List<Project> projects = FileDataDao.loadProjects();
        System.out.println("Before modifying projects: " + projects);

        // Find the project within the projects list
        Project project = null;
        for (Project proj : projects) {
            if (proj.getProjectId() == projectId) {
                project = proj;
                break;
            }
        }

        if (project != null) {
            // Create a new task
            Task newTask = new Task();
            newTask.setTaskId(generateTaskId());
            newTask.setTaskName(taskName);
            newTask.setActive(true);
            newTask.setDescription("Default Description"); // or set it as per your logic

            // Add the task to the project's list of tasks
            project.getTasks().add(newTask);

            // Update projects in JSON file
            FileDataDao.writeProjectsToFile(projects);
        } else {
            System.out.println("Project not found with ID: " + projectId);
        }
    }

    public void doSaveTaskToProject(int projectId, Task task) {
        List<Project> projects = FileDataDao.loadProjects();
        System.out.println("Before modifying projects: " + projects);

        // Find the project within the projects list
        Project project = null;
        for (Project proj : projects) {
            if (proj.getProjectId() == projectId) {
                project = proj;
                break;
            }
        }
        if (project != null) {
            // Create a new task

            // Add the task to the project's list of tasks
            project.getTasks().add(task);

            // Update projects in JSON file
            FileDataDao.writeProjectsToFile(projects);
        } else {
            System.out.println("Project not found with ID: " + projectId);
        }
    }

    // Method to modify task name based on task ID
    public void modifyTaskName(int taskId, String newTaskName) {
        List<Project> projects = FileDataDao.loadProjects();
        boolean taskFound = false;

        // Iterate through projects and update task name if found
        for (Project project : projects) {
            List<Task> tasks = project.getTasks();
            for (Task task : tasks) {
                if (task.getTaskId() == taskId) {
                    task.setTaskName(newTaskName);
                    taskFound = true;
                    break; // Exit inner loop once task is found and updated
                }
            }
            if (taskFound) {
                break; // Exit outer loop once task is found and updated
            }
        }

        if (taskFound) {
            // Update projects in JSON file
            FileDataDao.writeProjectsToFile(projects);
            System.out.println("Task with ID " + taskId + " modified successfully.");
        } else {
            System.out.println("Task with ID " + taskId + " not found.");
        }
    }

    public void deleteTaskById(int taskId) {
        List<Project> projects = FileDataDao.loadProjects();
        boolean taskFound = false;
        System.out.println("Before deleting task: " + projects);

        for (Project project : projects) {
            Iterator<Task> iterator = project.getTasks().iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (task.getTaskId() == taskId) {
                    iterator.remove();
                    taskFound = true;
                    break;
                }
            }
            if (taskFound) {
                break;
            }
        }

        if (taskFound) {
            // Update projects in JSON file
            FileDataDao.writeProjectsToFile(projects);
            System.out.println("After deleting task: " + projects);
            System.out.println("Task with ID " + taskId + " deleted successfully.");
        } else {
            System.out.println("Task with ID " + taskId + " not found.");
        }
    }

    public void doMoveTaskFromOneProjectToAnother(int taskId, int projectId) {
        Task task = getTaskByTaskId(taskId);

        deleteTaskById(taskId);

        doSaveTaskToProject(projectId, task);

    }

    public void markTaskAsDoOrDone(int taskId) {
        List<Project> projects = FileDataDao.loadProjects();
        boolean taskFound = false;

        // Iterate through projects and mark task as complete if found
        for (Project project : projects) {
            List<Task> tasks = project.getTasks();
            for (Task task : tasks) {
                if (task.getTaskId() == taskId) {
                    task.setActive(!task.isActive()); // Toggle the active state of the task
                    taskFound = true;
                    break; // Exit inner loop once task is found and updated
                }
            }
            if (taskFound) {
                break; // Exit outer loop once task is found and updated
            }
        }

        if (taskFound) {
            // Update projects in JSON file
            FileDataDao.writeProjectsToFile(projects);
            System.out.println("Task with ID " + taskId + " status toggled.");

        } else {
            System.out.println("Task with ID " + taskId + " not found.");
        }
    }

    public void doModifyTaskDescription(Task taskObj) {
        int taskId = taskObj.getTaskId();
        List<Project> projects = FileDataDao.loadProjects();
        boolean taskFound = false;

        // Iterate through projects and mark task as complete if found
        for (Project project : projects) {
            List<Task> tasks = project.getTasks();
            for (Task task : tasks) {
                if (task.getTaskId() == taskId) {
//                    task.setActive(!task.isActive()); // Toggle the active state of the task
                    task.setDescription(taskObj.getDescription());

                    taskFound = true;
                    break; // Exit inner loop once task is found and updated
                }
            }
            if (taskFound) {
                break; // Exit outer loop once task is found and updated
            }
        }

        if (taskFound) {
            // Update projects in JSON file
            FileDataDao.writeProjectsToFile(projects);
            System.out.println("Task with ID " + taskId + " status toggled.");

        } else {
            System.out.println("Task with ID " + taskId + " not found.");
        }
    }

    private int generateTaskId() {
        List<Project> projects = FileDataDao.loadProjects();
        int maxId = 0;
        for (Project project : projects) {
            for (Task task : project.getTasks()) {
                if (task.getTaskId() > maxId) {
                    maxId = task.getTaskId();
                }
            }
        }
        return maxId + 1;
    }

    public List<Task> sortTasksByName(List<Task> list) {
//        List<Task> list = getAllTasks();
        // Create a comparator to sort tasks by taskName
        Comparator<Task> taskNameComparator = Comparator.comparing(Task::getTaskName);

        // Sort the list using the comparator
        Collections.sort(list, taskNameComparator);

        return list;
    }

    public List<Task> sortTasksById(List<Task> list) {
//        List<Task> list = getAllTasks();
        // Create a comparator to sort tasks by taskId
        Comparator<Task> taskIdComparator = Comparator.comparingInt(Task::getTaskId);

        // Sort the list using the comparator
        Collections.sort(list, taskIdComparator);

        return list;
    }
}
