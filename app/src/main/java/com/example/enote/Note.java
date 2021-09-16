package com.example.enote;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;

/**
 * Note Class
 * This class holds a note
 */
public class Note {
    String title;
    Spannable note;

    /**
     * The getFileName Method
     * Purpose: A getter for FileName
     */
    public String getTitle() {
        return title;
    }

    /**
     * The getNote Method
     * Purpose: A getter for Note
     */
    public Spannable getNote() {
        return note;
    }

    /**
     * The setFileName Method
     * Purpose: A setter for FileName
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * The setNote Method
     * Purpose: A setter for Note
     */
    public void setNote(Spannable note) {
        this.note = note;
    }
}
