package net.gillessed.icarus.engine;

public interface Callback<T> {
    public void callback(T value) throws Exception;
}
