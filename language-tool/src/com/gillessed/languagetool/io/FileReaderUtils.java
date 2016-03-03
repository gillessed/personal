package com.gillessed.languagetool.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReaderUtils {
    private FileReaderUtils() {
        throw new UnsupportedOperationException();
    }

    public static List<String> readAsStringList(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static String readAsString(File file) throws IOException {
        StringBuilder str = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                str.append(line);
                str.append("\n");
            }
        }
        return str.toString();
    }
}
