package com.cinema.view.listener;

public interface PanelActionListener<T> {
    void onRefreshRequested();
    void onEditRequested(T item);
}
