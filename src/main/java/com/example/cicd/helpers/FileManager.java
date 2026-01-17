package com.example.cicd.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    public static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public static void writeFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, content.getBytes());
        System.out.println("File written: " + filePath);
    }

    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    public static void createDirectory(String dirPath) throws IOException {
        Files.createDirectories(Paths. get(dirPath));
    }
}