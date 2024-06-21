package com.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.model.entity.Project;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class FileDataDao {

    private static final String FILE_PATH = FileHandler.getDataFilePath();

    public static List<Project> loadProjects() {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            // If the file does not exist or is empty, return an empty list
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Project.class));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void writeProjectsToFile(List<Project> projects) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.CLOSE_CLOSEABLE.INDENT_OUTPUT); // Pretty-print JSON
        try {
            objectMapper.writeValue(new File(FILE_PATH), projects);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
