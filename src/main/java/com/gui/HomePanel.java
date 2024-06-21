package com.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model.entity.Task;
import com.model.entity.Project;
import com.model.dao.TaskDao;
import com.model.dao.ProjectDao;
import com.util.SwingUtil;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class HomePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private SwingUtil swingUtil = new SwingUtil();
    Font font = new Font("Arial", Font.BOLD, 20);
    private TaskDao taskDao = new TaskDao();
    private ProjectDao projectDao = new ProjectDao();
    private MyApp myApp;

    private JComboBox<String> projectComboBox;
    private DefaultComboBoxModel<String> projectComboBoxModel;

    private JList<String> taskList;
    private DefaultListModel<String> taskListModel;
    private JComboBox<String> sortByComboBox;

    private Map<String, List<Task>> projects;

    private JTextField newProjectField;
    private JTextField newTaskField;
    private JTextField searchField;
    private JCheckBox checkboxMarkTask;

    private JPopupMenu popupMenu;

    public HomePanel(MyApp myApp) {
        this.myApp = myApp;
        System.out.println("HomePanel constructor");

        // init
        // this.projectDao = new ProjectDao();
        // Projects properties
        projects = new HashMap<>();
        projectComboBoxModel = new DefaultComboBoxModel<>();
        projectComboBox = new JComboBox<>(projectComboBoxModel);
        newProjectField = new JTextField(15);

        // project buttons
        JButton addProjectButton = new JButton("Add Project", swingUtil.createImageIcon("/add-list.png"));
        JButton editProjectButton = new JButton("Edit Project", swingUtil.createImageIcon("/edit.png"));
        JButton deleteProjectButton = new JButton("Delete Project", swingUtil.createImageIcon("/delete.png"));

        // Projects Operations
        addProjectButton.addActionListener(e -> addProject());

        editProjectButton.addActionListener(e -> editProject());

        deleteProjectButton.addActionListener(e -> deleteProject());

        // Add a default project to avoid null selection
        String defaultProject = "Default Project";
        projects.put(defaultProject, new ArrayList<>());
        projectComboBoxModel.addElement(defaultProject);

//        adding projects to ui
        List<Project> list = projectDao.getAllProjects();
        for (Project p : list) {
            projects.put(p.getProjectName(), p.getTasks());
            projectComboBoxModel.addElement(p.getProjectName());
        }

        // task properies
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        newTaskField = new JTextField(15);

        // task buttons
        JButton addTaskButton = new JButton("Add Task", swingUtil.createImageIcon("/shopping-list.png"));

//        addTaskButton.setHorizontalTextPosition(SwingConstants.CENTER)
//        addTaskButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        JButton editTaskButton = new JButton("Edit Task", swingUtil.createImageIcon("/edit.png"));
        JButton deleteTaskButton = new JButton("Delete Task", swingUtil.createImageIcon("/delete.png"));
//        JButton markTaskButton = new JButton("Mark Task");
        checkboxMarkTask = new JCheckBox("Mark Task", swingUtil.createImageIcon("/checked.png"));

        // task Operations
        addTaskButton.addActionListener(e -> doAddTask());

        editTaskButton.addActionListener(e -> editTask());

        deleteTaskButton.addActionListener(e -> deleteTask());

//        markTaskButton.addActionListener(e -> doMarkTask());
        checkboxMarkTask.addActionListener(e -> doMarkTask());

        //        Other properties
        //		JLabel welcomeLabel = new JLabel("Welcome to the Tasks Tracker");
        //		welcomeLabel.setFont(font);
        //		welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        //		add(welcomeLabel, BorderLayout.CENTER);
//        JButton logoutButton = new JButton("Logout");
//        logoutButton.setFont(font);
//        logoutButton.addActionListener(e -> handleLogoutButton());
        // Right click menu properties
        popupMenu = new JPopupMenu();

        // Right click menu buttons
        JMenuItem showInfoTaskMenuItem = new JMenuItem("Show Info", swingUtil.createImageIcon("/info.png"));
        JMenuItem editTaskMenuItem = new JMenuItem("Edit Task", swingUtil.createImageIcon("/edit.png"));
        JMenuItem deleteTaskMenuItem = new JMenuItem("Delete Task", swingUtil.createImageIcon("/delete.png"));
        JMenuItem moveTaskMenuItem = new JMenuItem("Move Task", swingUtil.createImageIcon("/export.png"));
        JMenuItem markTaskMenuItem = new JMenuItem("Mark Task", swingUtil.createImageIcon("/ok_checked.png"));
        JMenuItem copyTaskMenuItem = new JMenuItem("Copy to clipboard", swingUtil.createImageIcon("/copy_to_clip.png"));

        popupMenu.add(showInfoTaskMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(copyTaskMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(markTaskMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(moveTaskMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(editTaskMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(deleteTaskMenuItem);

        // Add MouseListener to show the popup menu on right-click
        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int index = taskList.locationToIndex(e.getPoint());
                    if (index != -1) {
                        taskList.setSelectedIndex(index);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        // Right click menu operations
        showInfoTaskMenuItem.addActionListener(e -> showInfoTask());

        editTaskMenuItem.addActionListener(e -> editTask());

        deleteTaskMenuItem.addActionListener(e -> deleteTask());

        moveTaskMenuItem.addActionListener(e -> doMoveTaskToProject());

        markTaskMenuItem.addActionListener(e -> doMarkTask());
        copyTaskMenuItem.addActionListener(e -> doCopyToClipboard());

//        JButton searchButton = new JButton("Search By");
//        searchButton.addActionListener(e -> filterTasks());
        // Search field setup
//        JButton searchButton = new JButton("SearchTask");
//        searchButton.addActionListener(e -> filterTasks());
        searchField = new JTextField(15);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTasks();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTasks();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTasks();
            }
        });

        // data init
        projectComboBox.addActionListener(e -> loadTasksForSelectedProject());
        loadDefaultTasks();

//        sorting in tasks
        String[] sortOptions = {"Sort by", "Name", "ID"};
        sortByComboBox = new JComboBox<>(sortOptions);
        sortByComboBox.setSelectedIndex(0); // Default selection
        sortByComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSort = (String) sortByComboBox.getSelectedItem();
                if (!selectedSort.equals("Sort by")) {
                    //                        String selectedProject = (String) projectComboBox.getSelectedItem();
//                        System.out.println("Selected Project: " + selectedProject);
//                        int pId = projectDao.getProjectIdByProjectName(selectedProject);
//                        List<Task> tasks = new ArrayList<>();
//                        if (pId != 0) {
//                            Project project = projectDao.getProjectById(pId);
//                            tasks = project.getTasks();
//                        } else {
//                            tasks = taskDao.getAllTasks();
//                        }
//                        List<Task> list = taskDao.sortTasksByName(tasks);
                    if (selectedSort.equalsIgnoreCase("Name")) {
//                       
                        System.out.println("======= sorting by name");
                        loadTasksForSorting("NAME");

                    } else if (selectedSort.equalsIgnoreCase("ID")) {
//                        taskList.sortByTaskId();
                        System.out.println("======= sorting by ID");
                        loadTasksForSorting("ID");

                    }

                }
            }
        });

//        
        // Add list selection listener to taskList
        taskList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && taskList.getSelectedValue() != null) {
                    String selectedTask = taskList.getSelectedValue();
                    String selectedTaskName = selectedTask.replaceAll("<[^>]*>", ""); // Remove HTML tags
                    System.out.println("========= taskList.addListSelectionListener called");
                    updateCheckboxForTasks(selectedTaskName);

                }
            }
        });

        // UI Design
        //        setLayout(new BorderLayout());
        setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

//        JPanel upperPanel = new JPanel(new FlowLayout());
        JPanel upperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        upperPanel.setBorder(BorderFactory.createTitledBorder("Project Management"));
        upperPanel.add(new JLabel("Select Project:"));
        upperPanel.add(projectComboBox);
        upperPanel.add(newProjectField);
        upperPanel.add(addProjectButton);
        upperPanel.add(editProjectButton);
        upperPanel.add(deleteProjectButton);

        // Lower Panel
        // JPanel lowerPanel = new JPanel(new FlowLayout());
        JPanel lowerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        lowerPanel.setBorder(BorderFactory.createTitledBorder("Task Management"));
        lowerPanel.add(new JLabel("New Task:"));
        lowerPanel.add(newTaskField);
        lowerPanel.add(addTaskButton);
        lowerPanel.add(editTaskButton);
        lowerPanel.add(deleteTaskButton);
//        lowerPanel.add(markTaskButton);
        lowerPanel.add(checkboxMarkTask);

        // Create a scroll pane and add the task list to it
        JScrollPane tasksListPanel = new JScrollPane(taskList);
        this.add(tasksListPanel, BorderLayout.CENTER);

        // Search Panel
        // JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
//        searchPanel.add(searchButton);
        searchPanel.add(sortByComboBox);

//        JPanel topPanel = new JPanel(new BorderLayout());
//        topPanel.add(upperPanel, BorderLayout.NORTH);
//        add(upperPanel, BorderLayout.NORTH);
//        add(lowerPanel, BorderLayout.SOUTH);
//        add(searchPanel, BorderLayout.WEST);
        // Add Panels to Frame
//        setting ui properties
// Calculate intermediate color between LIGHT_GRAY and GRAY
        Color customColor = interpolateColors(Color.LIGHT_GRAY, Color.GRAY, 0.5f); // Change 0.5f for different shades
        setPreferredSize(new Dimension(800, 600));
        upperPanel.setBackground(Color.LIGHT_GRAY);
        searchPanel.setBackground(customColor);
        tasksListPanel.setBackground(Color.WHITE);
        lowerPanel.setBackground(Color.LIGHT_GRAY);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(upperPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
//        add(upperPanel, BorderLayout.NORTH);
//        add(searchPanel, BorderLayout.WEST);

        add(tasksListPanel, BorderLayout.CENTER);
        add(lowerPanel, BorderLayout.SOUTH);

    }

    private void doCopyToClipboard() {

        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedTask = taskListModel.getElementAt(selectedIndex);

            String textToCopy = selectedTask.replaceAll("<[^>]*>", ""); // Remove HTML tags

            // Copy text to clipboard
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(textToCopy);
            clipboard.setContents(selection, null);

            // Show success message
            JOptionPane.showMessageDialog(this, textToCopy + " - Text copied to clipboard successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Show error message if no task is selected
            JOptionPane.showMessageDialog(this, "No task selected to copy.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCheckboxForTasks(String taskName) {

        System.out.println("========= updateCheckboxForTasks called");
        int taskId = taskDao.getTaskIdByTaskName(taskName);
        Task task = taskDao.getTaskByTaskId(taskId);
        if (task != null && task.isActive() == false) {
            System.out.println(task.getTaskName() + "========= made marked");
            checkboxMarkTask.setSelected(true); // Mark checkbox as selected if "Task 2" is selected
        } else {
            System.out.println(task.getTaskName() + "========= made unmarked");
            checkboxMarkTask.setSelected(false); // Unmark checkbox otherwise
        }
    }

    private void showInfoTask() {

        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedTask = taskListModel.getElementAt(selectedIndex);

            String selectedTaskName = selectedTask.replaceAll("<[^>]*>", ""); // Remove HTML tags

            int taskId = taskDao.getTaskIdByTaskName(selectedTaskName);

            Task task = taskDao.getTaskByTaskId(taskId);

            // Example dialog with fields for editing task details
            JPanel panel = new JPanel(new GridLayout(0, 1));
            JTextField descriptionField = new JTextField();
            descriptionField.setText(task.getDescription()); // Display current task name in the text field

            panel.add(new JLabel("Task Name : " + task.getTaskName()));
            panel.add(descriptionField);

            int option = JOptionPane.showConfirmDialog(null, panel, "Edit Task", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                // Update task details
                String newDescriptionName = descriptionField.getText();
//                taskListModel.setElementAt(newTaskName, selectedIndex); // Update list model
//                taskDao.modifyTaskName(taskId, newTaskName);
                task.setDescription(newDescriptionName);
                taskDao.doModifyTaskDescription(task);

                // Display success message (you can integrate with your actual update logic)
                JOptionPane.showMessageDialog(null, "Task info updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task to show info", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    // Method to filter tasks based on search input
    private void filterTasks() {
        System.out.println("============== invoked :filterTasks");
        String searchTerm = searchField.getText().toLowerCase();
        taskListModel.clear();

        List<Task> list = taskDao.getAllTasks();
        List<String> tasks = new ArrayList<>();
        for (Task t : list) {
            tasks.add(t.getTaskName());
        }

        for (String task : tasks) {
            if (task.toLowerCase().contains(searchTerm)) {
                taskListModel.addElement(task);
            }
        }
    }

    // Method to handle task editing
    private void editTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedTask = taskListModel.getElementAt(selectedIndex);

            int taskId = taskDao.getTaskIdByTaskName(selectedTask);

            // Example dialog with fields for editing task details
            JPanel panel = new JPanel(new GridLayout(0, 1));
            JTextField titleField = new JTextField();
            titleField.setText(selectedTask); // Display current task name in the text field

            panel.add(new JLabel("Task Name:"));
            panel.add(titleField);

            int option = JOptionPane.showConfirmDialog(null, panel, "Edit Task", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                // Update task details
                String newTaskName = titleField.getText();
                taskListModel.setElementAt(newTaskName, selectedIndex); // Update list model
                taskDao.modifyTaskName(taskId, newTaskName);

                // Display success message (you can integrate with your actual update logic)
                JOptionPane.showMessageDialog(null, "Task updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task to edit", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteTask() {

        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedTask = taskListModel.getElementAt(selectedIndex);
            int taskId = taskDao.getTaskIdByTaskName(selectedTask);

            if (taskId != 0) {
                // Show confirmation dialog
                int response = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to delete the : " + selectedTask + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                // If user confirms deletion
                if (response == JOptionPane.YES_OPTION) {
                    taskListModel.removeElementAt(selectedIndex);

                    // Delete project from database
                    taskDao.deleteTaskById(taskId);

                    // Show success message
                    JOptionPane.showMessageDialog(null, "Removed successfully", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, "Please select a task to modify.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void doMoveTaskToProject() {
        int taskSelectedIndex = taskList.getSelectedIndex();
        int projectSelectedIndex = projectComboBox.getSelectedIndex();

        if (projectSelectedIndex != -1 && taskSelectedIndex != -1) {
            // Get the current project and task details
            String currentProject = (String) projectComboBox.getSelectedItem();
            int currentProjectId = projectDao.getProjectIdByProjectName(currentProject);

            String selectedTask = taskListModel.getElementAt(taskSelectedIndex);
            int taskId = taskDao.getTaskIdByTaskName(selectedTask);
            Task task = taskDao.getTaskByTaskId(taskId);

            // Prompt user to select the target project
            String[] projectNames = projects.keySet().toArray(new String[0]);
            String selectedProject = (String) JOptionPane.showInputDialog(null,
                    "Select the project to move the task to:",
                    "Move Task",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    projectNames,
                    projectNames[0]);

            // Validate the selected project and perform the move
            if (selectedProject != null && !selectedProject.isEmpty()) {
                if (!currentProject.equals(selectedProject)) {
                    int targetProjectId = projectDao.getProjectIdByProjectName(selectedProject);

                    // Move task in the database
                    taskDao.doMoveTaskFromOneProjectToAnother(taskId, targetProjectId);

                    // Update the UI and in-memory data structure
                    taskListModel.removeElement(selectedTask); // Remove task from current project
                    projects.get(currentProject).remove(selectedTask); // Remove task from current project list
                    projects.get(selectedProject).add(task); // Add task to selected project list
//                    updateTaskList(selectedProject);
                    loadTasksForSelectedProject();

                    JOptionPane.showMessageDialog(null, "Task moved successfully", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Task is already in the selected project.", "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a project and a task.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshProjectsFromDatabase() {
        // Assuming ProjectDao has a method like getAllProjects() that returns a list of Project objects
        List<Project> projectsFromDB = projectDao.getAllProjects();

        // Clear existing items in the combo box model
        projectComboBoxModel.removeAllElements();

        // Clear existing projects map
        projects.clear();

        // Add fetched projects to combo box model and projects map
        for (Project project : projectsFromDB) {
            String projectName = project.getProjectName();
            List<Task> tasks = taskDao.getTasksForProject(project.getProjectId()); // Assuming TaskDao can fetch tasks by project ID
            projects.put(projectName, tasks);
            projectComboBoxModel.addElement(projectName);
        }

        // Reset the selected index in the combo box (if needed)
        projectComboBox.setSelectedIndex(-1); // To reset selection

        // Optionally, load tasks for the initially selected project
        loadTasksForSelectedProject();
    }

    public void loadTasksForSelectedProject() {
//        sortByComboBox.setSelectedIndex(-1);
        sortByComboBox.setSelectedItem("Sort by");
        String selectedProject = (String) projectComboBox.getSelectedItem();
        System.out.println("Selected Project: " + selectedProject);
        // Clear existing tasks in the list model

        if (selectedProject != null) {

            if ("Default Project".equals(selectedProject)) {
                loadDefaultTasks();
                return;
            }
            taskListModel.clear();
            int pId = projectDao.getProjectIdByProjectName(selectedProject);
            Project project = projectDao.getProjectById(pId);
            List<Task> tasks = project.getTasks();
            List<String> activeTasks = new ArrayList<>();
            List<String> inactiveTasks = new ArrayList<>();

            if (tasks != null) {
                for (Task task : tasks) {
                    if (task.isActive()) {
                        activeTasks.add(task.getTaskName());
//                        taskListModel.addElement(task.getTaskName());
                    } else {
                        inactiveTasks.add("<html><strike>" + task.getTaskName() + "</strike></html>");
//                        taskListModel.addElement("<html><strike>" + task.getTaskName() + "</strike></html>");
                    }

                }
            }
            // Add active tasks to the model
            for (String task : activeTasks) {
                taskListModel.addElement(task);
            }

            // Add a separator or header for inactive tasks if needed
//            if (!inactiveTasks.isEmpty()) {
//                taskListModel.addElement("---- Inactive Tasks ----");
//            }
            // Add inactive tasks to the model
            for (String task : inactiveTasks) {
                taskListModel.addElement(task);
            }

        }
        myApp.revalidate();
        myApp.repaint();
    }

    public void loadTasksForDefaultProject() {
        // Clear existing tasks in the list model
        taskListModel.clear();

        List<Task> tasks = taskDao.getAllTasks();
        List<String> activeTasks = new ArrayList<>();
        List<String> inactiveTasks = new ArrayList<>();

        if (tasks != null) {
            for (Task task : tasks) {
                if (task.isActive()) {
                    activeTasks.add(task.getTaskName());
//                        taskListModel.addElement(task.getTaskName());
                } else {
                    inactiveTasks.add("<html><strike>" + task.getTaskName() + "</strike></html>");
//                        taskListModel.addElement("<html><strike>" + task.getTaskName() + "</strike></html>");
                }

            }
        }
        // Add active tasks to the model
        for (String task : activeTasks) {
            taskListModel.addElement(task);
        }

        // Add a separator or header for inactive tasks if needed
//            if (!inactiveTasks.isEmpty()) {
//                taskListModel.addElement("---- Inactive Tasks ----");
//            }
        // Add inactive tasks to the model
        for (String task : inactiveTasks) {
            taskListModel.addElement(task);
        }

    }

    public void loadTasksForSorting(String sortFor) {

        String selectedProject = (String) projectComboBox.getSelectedItem();
        System.out.println("Selected Project: " + selectedProject);
        List<Task> list = new ArrayList<>();
        // Clear existing tasks in the list model

        if (selectedProject != null) {

            if ("Default Project".equalsIgnoreCase(selectedProject)) {
                list = taskDao.getAllTasks();
            } else {
                int pId = projectDao.getProjectIdByProjectName(selectedProject);
                Project project = projectDao.getProjectById(pId);
                list = project.getTasks();
            }

            taskListModel.clear();

            if (list != null) {
                if (sortFor.equalsIgnoreCase("ID")) {
                    System.out.println("========== inside sorting and loading by ID");
                    list = taskDao.sortTasksById(list);
                } else if (sortFor.equals("NAME")) {
                    System.out.println("========== inside sorting and loading by NAME");
                    list = taskDao.sortTasksByName(list);
                }

                for (Task task : list) {
                    if (task.isActive()) {

                        taskListModel.addElement(task.getTaskName());
                    } else {

                        taskListModel.addElement("<html><strike>" + task.getTaskName() + "</strike></html>");
                    }

                }
            }

        }

    }

    public void doMarkTask() {

        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {

            String selectedTaskHtml = taskListModel.getElementAt(selectedIndex);
            String selectedTaskName = selectedTaskHtml.replaceAll("<[^>]*>", ""); // Remove HTML tags
//            String selectedTask = taskListModel.getElementAt(selectedIndex);
            System.out.println("selectedTask=============" + selectedTaskName);
            int taskId = taskDao.getTaskIdByTaskName(selectedTaskName);
            Task task = taskDao.getTaskByTaskId(taskId);
            String message = "";

            if (task.isActive()) {
                message = "Are you sure you want to mark this task as completed?";
            } else {
                message = "Are you sure you want to mark this task as uncompleted?";
            }

            // Display confirmation dialog
            int option = JOptionPane.showConfirmDialog(null,
                    message,
                    "Confirm Mark Task",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                // Proceed with marking the task as complete
                taskDao.markTaskAsDoOrDone(taskId);
                JOptionPane.showMessageDialog(null, "Task Marked Successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task to mark as complete.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        loadTasksForSelectedProject();
//        refreshProjectsFromDatabase();
    }

    public void loadDefaultTasks() {

        List<Task> tasks = taskDao.getAllTasks();
        List<String> activeTasks = new ArrayList<>();
        List<String> inactiveTasks = new ArrayList<>();

        if (tasks != null) {
            for (Task task : tasks) {
                if (task.isActive()) {
                    activeTasks.add(task.getTaskName());
//                        taskListModel.addElement(task.getTaskName());
                } else {
                    inactiveTasks.add("<html><strike>" + task.getTaskName() + "</strike></html>");
//                        taskListModel.addElement("<html><strike>" + task.getTaskName() + "</strike></html>");
                }

            }
        }
        // Add active tasks to the model
        for (String task : activeTasks) {
            taskListModel.addElement(task);
        }

        // Add a separator or header for inactive tasks if needed
//            if (!inactiveTasks.isEmpty()) {
//                taskListModel.addElement("---- Inactive Tasks ----");
//            }
        // Add inactive tasks to the model
        for (String task : inactiveTasks) {
            taskListModel.addElement(task);
        }
        myApp.revalidate();
        myApp.repaint();
    }

    public void doAddTask() {
        String selectedProject = (String) projectComboBox.getSelectedItem();
        String newTask = newTaskField.getText().trim();

        if (selectedProject != null && !newTask.isEmpty()) {
            Task task = new Task();
            task.setTaskName(newTask);
            projects.get(selectedProject).add(task);
            ProjectDao pDao = new ProjectDao();
            int projectId = pDao.getProjectIdByProjectName(selectedProject);
            taskDao.addTaskToProject(projectId, newTask);

            newTaskField.setText("");
//                    updateTaskList(selectedProject);
            loadTasksForSelectedProject();

        } else {
            JOptionPane.showMessageDialog(myApp, "Select a project and enter a task.");
        }
    }

    public void deleteProject() {

        int selectedIndex = projectComboBox.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedProject = (String) projectComboBox.getSelectedItem();
            int projectId = projectDao.getProjectIdByProjectName(selectedProject);

            if (projectId != 0) {
                // Show confirmation dialog
                int response = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to delete the project: " + selectedProject + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                // If user confirms deletion
                if (response == JOptionPane.YES_OPTION) {
                    // Update projects map and combo box model
                    projects.remove(selectedProject);
                    projectComboBoxModel.removeElement(selectedProject);

                    // Delete project from database
                    projectDao.deleteProject(projectId);

                    // Show success message
                    JOptionPane.showMessageDialog(null, "Removed successfully", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a project to modify.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public void editProject() {

        int selectedIndex = projectComboBox.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedProject = (String) projectComboBox.getSelectedItem();
            int projectId = projectDao.getProjectIdByProjectName(selectedProject);
            String editedProject = JOptionPane.showInputDialog(null, "Enter new project name:", selectedProject);
            if (editedProject != null && !editedProject.isEmpty()) {
                // Update projects map and combo box model
                projects.remove(selectedProject);
                projects.put(editedProject, new ArrayList<>());
                projectComboBoxModel.removeElement(selectedProject);
                projectComboBoxModel.addElement(editedProject);
                projectDao.modifyProjectName(projectId, editedProject);

                JOptionPane.showMessageDialog(null, "Project edited successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a project to edit.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public void addProject() {

        String newProject = newProjectField.getText().trim();
        if (!newProject.isEmpty()) {
            projects.put(newProject, new ArrayList<>());
            projectComboBoxModel.addElement(newProject);
            projectDao.addProject(newProject);
            JOptionPane.showMessageDialog(myApp, "Project added successfully", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            newProjectField.setText("");
        } else {
            JOptionPane.showMessageDialog(myApp, "Project name cannot be empty.");
        }
    }

    public static Color interpolateColors(Color c1, Color c2, float ratio) {
        float r = (float) (c1.getRed() * (1 - ratio) + c2.getRed() * ratio) / 255;
        float g = (float) (c1.getGreen() * (1 - ratio) + c2.getGreen() * ratio) / 255;
        float b = (float) (c1.getBlue() * (1 - ratio) + c2.getBlue() * ratio) / 255;
        return new Color(r, g, b);
    }

}
