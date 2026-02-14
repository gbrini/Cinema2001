package com.cinema.util;

public interface DialogCloseObserver {
    /**
     * Called by the subject (JDialog) when it's about to close
     */
    void onDialogClosed(boolean changedSaved);
}
