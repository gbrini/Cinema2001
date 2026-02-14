package com.cinema.util;

public interface Observable<T> {
    void addObserver(T observer);
    void deleteObserver(T observer);
    void notifyObservers(boolean changedSaved);
}
