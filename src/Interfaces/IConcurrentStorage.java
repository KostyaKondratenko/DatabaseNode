package Interfaces;

public interface IConcurrentStorage<T> {
    void add(T value);
    void remove(T value);
}

