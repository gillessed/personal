package com.gillessed.languagetool.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gillessed.languagetool.model.Word;

public class ArabicLineReader implements Reader {

    private final File file;

    public ArabicLineReader(File file) {
        this.file = file;
    }

    @Override
    public List<Word> read() throws IOException {
        List<String> lines = FileReaderUtils.readAsStringList(file);
        List<Word> words = new ArrayList<>();
        for(int i = 0; i < lines.size(); i+= 2) {
            words.add(new Word("Arabic", lines.get(i), lines.get(i+1)));
        }
        return words;
    }
}