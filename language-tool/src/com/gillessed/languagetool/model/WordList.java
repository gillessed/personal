package com.gillessed.languagetool.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gillessed.languagetool.io.Reader;

public class WordList {
    private final List<Word> words;
    private final List<Reader> sources;
    private boolean ready;
    public WordList() {
        words = new ArrayList<>();
        sources = new ArrayList<>();
        ready = false;
    }

    public void addSource(Reader reader) {
        sources.add(reader);
    }

    public void read() {
        for(Reader reader : sources) {
            try {
                words.addAll(reader.read());
            } catch(IOException e) {
                System.err.println("Error reading from reader.");
            }
        }
        setReady(true);
    }

    private synchronized void setReady(boolean ready) {
        this.ready = ready;
    }

    public synchronized boolean isReady() {
        return ready;
    }

    public List<Word> getWordList() {
        return Collections.unmodifiableList(words);
    }
}
