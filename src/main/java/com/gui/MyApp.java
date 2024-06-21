package com.gui;

import com.model.dao.TaskDao;
import com.model.entity.Task;
import com.model.entity.Project;
import com.model.dao.ProjectDao;
import com.util.FileHandler;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.util.SwingUtil;
import java.io.FileWriter;
import java.net.URL;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;

public class MyApp extends JFrame {

    private static final long serialVersionUID = 1L;
    private SwingUtil swingUtil = new SwingUtil();
//    private CardLayout cardLayout;
//    private JPanel mainPanel;
    private JPanel currentPanel; // To hold the current visible panel
    private LoginPanel loginPanel;
    private HomePanel homePanel;
    private boolean isLoggedIn = false; // Flag to track login state
    private JMenuBar menuBar;

    public MyApp() {
        initialize();
        setupUI();
        initializeComponents();
        showLoginPanel();

    }

    private void initialize() {
        FileHandler.setupDataFile(); // Initialize the data file setup

        setTitle("Tasks Tracker System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
    }

    private void setupUI() {
        loginPanel = new LoginPanel(this);
        homePanel = new HomePanel(this);

        currentPanel = loginPanel;
//        cardLayout = new CardLayout();
//        mainPanel = new JPanel(cardLayout);

//        mainPanel.add(loginPanel, "Login");
//        mainPanel.add(homePanel, "Home");
//        add(mainPanel, BorderLayout.CENTER);
        ImageIcon icon = swingUtil.createImageIcon("/icon.png");
        // Set the icon for the JFrame
        if (icon != null) {
            setIconImage(icon.getImage());
        }

        add(currentPanel, BorderLayout.CENTER);
        pack(); // Adjust window size based on preferred sizes of its subcomponents
    }

    private void initializeComponents() {
        //        menu
// Create menu bar
        menuBar = new JMenuBar();

        // Create File menu
        JMenu fileMenu = new JMenu("File");
//        fileMenu = new JMenu("filed");
        JMenu editMenu = new JMenu("Edit");
        // Create Logout menu item
        JMenuItem logoutItem = new JMenuItem("Logout", swingUtil.createImageIcon("/reset.png"));
        logoutItem.addActionListener(e -> logout());

        JMenuItem exportMenuItem = new JMenuItem("Export", swingUtil.createImageIcon("/export_all.png"));
        JMenuItem importMenuItem = new JMenuItem("Import", swingUtil.createImageIcon("/import_all.png"));

        exportMenuItem.addActionListener(e -> doStartExport());
        importMenuItem.addActionListener(e -> doStartImport());

        // Create Exit menu item
        JMenuItem exitItem = new JMenuItem("Exit", swingUtil.createImageIcon("/exit.png"));
        exitItem.addActionListener(e -> exit());

        // Add menu items to File menu
        fileMenu.add(logoutItem);
        fileMenu.addSeparator(); // Optional separator between items
        fileMenu.add(exitItem);

        // Add edit menu items
        editMenu.add(exportMenuItem);
        editMenu.addSeparator();
        editMenu.add(importMenuItem);

        // Add File menu to menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        this.setJMenuBar(menuBar);
    }

    private void doStartImport() {
        importTasksFromJsonFile();
    }

    private void doStartExport() {
        showTaskTableDialogForExport();
    }

//    private void importTasksFromJsonFile() {
//        JFileChooser fileChooser = new JFileChooser();
//        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                Project[] importedProjects = objectMapper.readValue(file, Project[].class);
//                projects.clear();
//                taskListModel.clear();
//                for (Project project : importedProjects) {
//                    projects.add(project);
//                    for (Task task : project.getTasks()) {
//                        taskListModel.addElement(task);
//                    }
//                }
//                JOptionPane.showMessageDialog(this, "Tasks imported successfully.");
//            } catch (IOException ex) {
//                JOptionPane.showMessageDialog(this, "Error importing tasks: " + ex.getMessage());
//            }
//        }
//    }
    private void importTasksFromJsonFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // Change this path to the directory where you want to save the imported file
//                String importDirectory = "/path/to/your/import/directory/";
                String importPath = FileHandler.getDataFilePath();

                // Get the filename without path to save in the import directory
//                String filename = selectedFile.getName();
                // Prepare the file destination path
                File importFile = new File(importPath);

                // Copy the selected file to the import directory
                selectedFile.renameTo(importFile);

                JOptionPane.showMessageDialog(this, "File imported successfully to " + importFile.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error importing file: " + ex.getMessage());
            }
        }
    }

    private void exportTasksToTextFile(List<Project> projects, JDialog dialog) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("tasks.txt"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(file)) {
                for (Project project : projects) {
                    writer.write("Project ID: " + project.getProjectId() + "\n");
                    writer.write("Project Name: " + project.getProjectName() + "\n");
                    for (Task task : project.getTasks()) {
                        writer.write("\tTask ID: " + task.getTaskId() + "\n");
                        writer.write("\tTask Name: " + task.getTaskName() + "\n");
                        writer.write("\tDescription: " + task.getDescription() + "\n");
                        writer.write("\tActive: " + task.isActive() + "\n\n");
                    }
                }
                JOptionPane.showMessageDialog(this, "Tasks exported successfully.");
                dialog.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting tasks: " + ex.getMessage());
            }
        }
    }

    private void exportTasksToJsonFile(List<Project> projects, JDialog dialog) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("tasks.json"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, projects);
                JOptionPane.showMessageDialog(this, "Tasks exported successfully.");
                dialog.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting tasks: " + ex.getMessage());
            }
        }
    }

    public void showTaskTableDialogForExport() {
        System.out.println("====== showTaskTableDialogForExport in Export ===========");

        // Create a JDialog to hold the table
        JDialog dialog = new JDialog(this, "Tasks Data Table", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);

        // Create a DefaultTableModel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Task ID");
        model.addColumn("Name");
        model.addColumn("Description");
        model.addColumn("Active Status");
        TaskDao taskDao = new TaskDao();

        List<Task> tasks = taskDao.getAllTasks();

        // Add tasks to the model
        for (Task task : tasks) {
            model.addRow(new Object[]{task.getTaskId(), task.getTaskName(), task.getDescription(), task.isActive()});
        }

        // Create a JTable with the model
        JTable table = new JTable(model);

        // Export
        // Create radio buttons for export type
        JRadioButton textRadioButton = new JRadioButton("Text", true);
        JRadioButton jsonRadioButton = new JRadioButton("JSON");

        // Group the radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(textRadioButton);
        group.add(jsonRadioButton);

        // Create a panel for the radio buttons
        JPanel radioPanel = new JPanel();
        radioPanel.add(textRadioButton);
        radioPanel.add(jsonRadioButton);

        // Create buttons
//        JButton exportTextButton = new JButton("Export as Text");
//        JButton exportJsonButton = new JButton("Export as JSON");
// Create buttons
        JButton exportButton = new JButton("Export");
        JButton cancelButton = new JButton("Cancel");

        ProjectDao projectDao = new ProjectDao();
        List<Project> data = projectDao.getAllProjects();
        // Add ActionListeners for the buttons
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textRadioButton.isSelected()) {
                    exportTasksToTextFile(data, dialog);
                } else {
                    exportTasksToJsonFile(data, dialog);
                }
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exportButton);
        buttonPanel.add(cancelButton);
        // Add the button panel to the dialog
        // Add the radio panel and button panel to the dialog
        dialog.add(radioPanel, BorderLayout.NORTH);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        // Add the table to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);
        // Show the dialog
        dialog.setVisible(true);

//        frame.add(scrollPane, BorderLayout.CENTER);
    }

    public void showLoginPanel() {
        currentPanel = loginPanel;
        setContentPane(currentPanel);
        isLoggedIn = false;
//        cardLayout.show(mainPanel, "Login");
        revalidate(); // Refresh the frame
        pack();
        setSize(400, 400); // Adjust size to fit LoginPanel's preferred size
        setLocationRelativeTo(null); // Center the window on the screen
        // Ensure menuBar is initialized and then update file menu visibility
        if (menuBar != null) {
            updateFileMenuVisibility();
        }

        // Hide the file menu when showing the login panel
//        menuBar.setVisible(false);
    }

    public void showHomePanel() {
//        cardLayout.show(mainPanel, "Home");
        currentPanel = homePanel;
        setContentPane(currentPanel);
        isLoggedIn = true;
        revalidate(); // Refresh the frame
        pack(); // Adjust window size based on preferred sizes of its subcomponents
        setLocationRelativeTo(null); // Center the window on the screen
        // Ensure menuBar is initialized and then update file menu visibility
        if (menuBar != null) {
            updateFileMenuVisibility();
        }
    }

    // Method to switch to LoginPanel after logout
    private void logout() {
        // Perform logout actions (clear session, reset UI, etc.)
        // For example, here we switch back to the LoginPanel
//        switchToLoginPanel();
        showLoginPanel();
    }

    private void exit() {
        // Perform exit actions
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?");
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0); // Exit the application
        }
    }

    private void updateFileMenuVisibility() {
        // Show file menu only if user is logged in and HomePanel is active
//        menuBar.setVisible(isLoggedIn && currentPanel.getComponent(0) == homePanel);
        menuBar.setVisible(isLoggedIn);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MyApp app = new MyApp();
            app.setVisible(true); // Show the frame after initialization
        });
    }

}
