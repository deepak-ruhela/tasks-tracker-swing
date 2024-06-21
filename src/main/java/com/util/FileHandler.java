package com.util;

import java.io.File;
import java.io.IOException;

public class FileHandler {

    public static void setupDataFile() {
        String userHome = System.getProperty("user.home");
        String appDataPath = userHome + "/MyAppData";
        File appDataDir = new File(appDataPath);

        if (!appDataDir.exists()) {
            appDataDir.mkdir();
            System.out.println("Directory created: " + appDataPath);
        }

        File dataFileAuth = new File(appDataPath, "auth.txt");
        File dataFileTask = new File(appDataPath, "tasks.txt");
        File dataFileTaskList = new File(appDataPath, "project.txt");
        File dataFile = new File(appDataPath, "data.json");

        if (!dataFileAuth.exists()) {
            try {
                dataFileAuth.createNewFile();
                System.out.println("File created: " + dataFileAuth.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
                System.out.println("File created: " + dataFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!dataFileTask.exists()) {
            try {
                dataFileTask.createNewFile();
                System.out.println("File created: " + dataFileTask.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!dataFileTaskList.exists()) {
            try {
                dataFileTaskList.createNewFile();
                System.out.println("File created: " + dataFileTaskList.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getAuthFilePath() {
        String userHome = System.getProperty("user.home");
        String appDataPath = userHome + "/MyAppData";
        return appDataPath + "/auth.txt";
    }

    public static String getTasksFilePath() {
        String userHome = System.getProperty("user.home");
        String appDataPath = userHome + "/MyAppData";
        return appDataPath + "/tasks.txt";
    }

    public static String getProjectFilePath() {
        String userHome = System.getProperty("user.home");
        String appDataPath = userHome + "/MyAppData";
        return appDataPath + "/project.txt";
    }

    public static String getDataFilePath() {
        String userHome = System.getProperty("user.home");
        String appDataPath = userHome + "/MyAppData";
        return appDataPath + "/data.json";
    }
}
