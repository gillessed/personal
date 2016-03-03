package com.gillessed.languagetool.model;

import com.gillessed.optional.Optional;

public class Word {
    private final String language;
    private final String text;
    private final Optional<String> reading;
    private final String definition;

    public Word(String language, String text, String definition) {
        this.language = language;
        this.text = text;
        this.reading = Optional.absent();
        this.definition = definition;
    }

    public Word(String language, String text, String reading, String definition) {
        this.language = language;
        this.text = text;
        this.reading = Optional.of(reading);
        this.definition = definition;
    }

    public String getLanguage() {
        return language;
    }

    public String getText() {
        return text;
    }

    public Optional<String> getReading() {
        return reading;
    }

    public String getDefinition() {
        return definition;
    }
}
