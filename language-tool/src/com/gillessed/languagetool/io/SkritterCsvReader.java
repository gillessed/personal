package com.gillessed.languagetool.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gillessed.languagetool.model.Word;

public class SkritterCsvReader implements Reader {

    private final File file;
    private final String language;

    public SkritterCsvReader(File file, String language) {
        this.file = file;
        this.language = language;
    }

    @Override
    public List<Word> read() throws IOException {
        String str = FileReaderUtils.readAsString(file);
        CSVParser csvParser = new CSVParser('\t');
        List<List<String>> table = csvParser.parse(str);
        List<Word> words = new ArrayList<>();
        for(List<String> row : table) {
            if(row.size() < 2) {
                throw new IllegalStateException("Row " + row + " should have at least 2 elements. ");
            } else if(row.size() == 2) {
                words.add(new Word(
                        language,
                        row.get(0),
                        row.get(1)));
            } else {
                words.add(new Word(
                        language,
                        row.get(0),
                        row.get(1),
                        row.get(2)));
            }
        }
        return words;
    }
}
