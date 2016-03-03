package com.gillessed.languagetool.io;

import java.io.IOException;
import java.util.List;

import com.gillessed.languagetool.model.Word;

public interface Reader {
    public List<Word> read() throws IOException;
}
