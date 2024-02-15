package com.example.employees.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
@Component
public class FileReader {
        public List<String> read(String filePath) {
            List<String> content = new ArrayList<>();

            File file = new File(filePath);
            try (BufferedReader reader = new BufferedReader(new java.io.FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        content.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return content;
        }
}
