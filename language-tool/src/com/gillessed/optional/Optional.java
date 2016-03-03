package com.gillessed.optional;

public class Optional<T> {
    private final T value;
    private final boolean isPresent;

    private Optional() {
        value = null;
        isPresent = false;
    }

    private Optional(T value) {
        this.value = value;
        isPresent = true;
    }

    public T get() {
        return value;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public static <T> Optional<T> of(T value) {
        return new Optional<>(value);
    }

    public static <T> Optional<T> fromNullable(T value) {
        if(value == null) {
            return new Optional<>();
        } else {
            return new Optional<>(value);
        }
    }

    public static <T> Optional<T> absent() {
        return new Optional<T>();
    }
}
