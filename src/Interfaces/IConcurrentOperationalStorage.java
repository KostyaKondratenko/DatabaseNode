package Interfaces;

public interface IConcurrentOperationalStorage<T> extends IConcurrentStorage<T> {
    T get(int index);

    int count();

    T max();

    T min();
}
