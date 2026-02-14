package com.cinema.model;

import com.cinema.util.ComboBoxMethods;

public class Screen implements ComboBoxMethods {
    private int screenId;
    private String screenName;
    private int capacity;
    private boolean isDeleted;

    public Screen(int screenId, String screenName, int capacity, boolean isDeleted) {
        this.screenId = screenId;
        this.screenName = screenName;
        this.capacity = capacity;
        this.isDeleted = isDeleted;
    }

    public Screen(String screenName, int capacity, boolean isDeleted) {
        this.screenName = screenName;
        this.capacity = capacity;
        this.isDeleted = isDeleted;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getcapacity() {
        return capacity;
    }

    public void setcapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    @Override
    public String toStringComboBox() {
        return String.format("%d - %s", this.screenId, this.screenName);
    }
}
