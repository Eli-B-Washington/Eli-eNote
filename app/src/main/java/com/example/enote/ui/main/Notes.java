package com.example.enote.ui.main;

import java.util.Map;

/**
 * Notes Class
 * This class holds a map of Notes
 */
public class Notes {
    Map<String, String> notes;

    /**
     * The Notes Method
     * Purpose: A getter for notes
     * @param notes - map, string note
     */
    public Notes(Map<String, String> notes){
        this.notes = notes;
    }

    /**
     * The getNotes Method
     * Purpose: A getter for notes
     * @return notes
     */
    public Map<String, String> getNotes() {
        return notes;
    }

    /**
     * The setNotes Method
     * Purpose: A setter for notes
     * @param notes - map, string note
     */
    public void setNotes(Map<String, String> notes) {
        this.notes = notes;
    }
}
