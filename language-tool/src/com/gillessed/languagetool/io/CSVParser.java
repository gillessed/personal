package com.gillessed.languagetool.io;

import java.util.ArrayList;
import java.util.List;

public class CSVParser {
    private final char delimiter;

    public CSVParser(char delimiter) {
        this.delimiter = delimiter;
    }

    public List<List<String>> parse(String text) {
        List<List<String>> table = new ArrayList<>();
        List<String> row = new ArrayList<>();
        int index = 0;
        boolean inQuotes = false;
        StringBuilder currentToken = new StringBuilder();
        while(index < text.length()) {
            char ch = text.charAt(index);
            if(inQuotes) {
                if(ch == '"') {
                    inQuotes = false;
                } else if(ch == '\n') {
                    currentToken.append(" ");
                } else {
                    currentToken.append(ch);
                }
            } else {
                if(ch == delimiter) {
                    if(currentToken.length() != 0) {
                        row.add(currentToken.toString());
                    }
                    currentToken = new StringBuilder();
                } else if(ch == '"') {
                    inQuotes = true;
                } else if(ch == '\n') {
                    if(currentToken.length() != 0) {
                        row.add(currentToken.toString());
                    }
                    if(!row.isEmpty()) {
                        table.add(row);
                    }
                    row = new ArrayList<>();
                    currentToken = new StringBuilder();
                } else {
                    currentToken.append(ch);
                }
            }
            index++;
        }
        if(currentToken.length() > 0) {
            row.add(currentToken.toString());
        }
        if(row.size() > 0) {
            table.add(row);
        }
        return table;
    }
}
