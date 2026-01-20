package com.example.cicd.helpers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    @TempDir
    Path tempDir;

    private Path testFilePath;

    @BeforeEach
    void setUp() {
        testFilePath = tempDir.resolve("test-file.txt");
    }

    @Test
    void testWriteFile_Success() throws IOException {
        String content = "Hello, World!";
        
        FileManager.writeFile(testFilePath.toString(), content);
        
        assertTrue(Files.exists(testFilePath));
        String readContent = Files.readString(testFilePath);
        assertEquals(content, readContent);
    }

    @Test
    void testWriteFile_WithMultipleLines() throws IOException {
        String content = "Line 1\nLine 2\nLine 3";
        
        FileManager.writeFile(testFilePath.toString(), content);
        
        String readContent = Files.readString(testFilePath);
        assertEquals(content, readContent);
    }

    @Test
    void testWriteFile_OverwritesExistingFile() throws IOException {
        String initialContent = "Initial content";
        String newContent = "New content";
        
        FileManager.writeFile(testFilePath.toString(), initialContent);
        FileManager.writeFile(testFilePath.toString(), newContent);
        
        String readContent = Files.readString(testFilePath);
        assertEquals(newContent, readContent);
    }

    @Test
    void testWriteFile_CreatesDirectories() throws IOException {
        Path nestedPath = tempDir.resolve("nested/dir/file.txt");
        String content = "Test content";
        
        FileManager.writeFile(nestedPath.toString(), content);
        
        assertTrue(Files.exists(nestedPath));
        assertEquals(content, Files.readString(nestedPath));
    }

    @Test
    void testWriteFile_EmptyContent() throws IOException {
        String content = "";
        
        FileManager.writeFile(testFilePath.toString(), content);
        
        assertTrue(Files.exists(testFilePath));
        assertEquals(content, Files.readString(testFilePath));
    }

    @Test
    void testWriteFile_SpecialCharacters() throws IOException {
        String content = "Special: @#$%^&*()_+-=[]{}|;':\",./<>?";
        
        FileManager.writeFile(testFilePath.toString(), content);
        
        String readContent = Files.readString(testFilePath);
        assertEquals(content, readContent);
    }

    @Test
    void testWriteFile_XMLContent() throws IOException {
        String xmlContent = "<?xml version=\"1.0\"?>\n<root><element>value</element></root>";
        
        FileManager.writeFile(testFilePath.toString(), xmlContent);
        
        String readContent = Files.readString(testFilePath);
        assertEquals(xmlContent, readContent);
    }
}