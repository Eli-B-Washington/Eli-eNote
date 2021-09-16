package com.example.enote.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;

import androidx.annotation.RequiresApi;

import com.example.enote.Note;
import com.example.enote.R;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * FileHelper Class
 * This class is responsible for saving, loading, and deleting notes
 */

public class FileHelper {
    public FileHelper(Activity activity){
        this.activity = activity;
    }

    Note myNote = new Note();
    Activity activity;
    Notes noteMap;

    /**
     * The save Method
     * Purpose: to save the note
     * @param  title, The title of the note
     * @param text, The contents of the note
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void save(String title, Spannable text) {

        myNote.setTitle(title);

        myNote.setNote(text);

        noteMap = openAll();
        String html = Html.toHtml(myNote.getNote());//, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);

        noteMap.getNotes().put(title, html);

        Gson gson = new Gson();
        String jsonString = gson.toJson(noteMap);
        SharedPreferences sharedPref = activity.getSharedPreferences(activity.getString(R.string.saved_notes), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.saved_notes_key), jsonString);
        editor.apply();
    }

    /**
     * The open Method
     * Purpose: to open a note
     * @param  title - The title of the note
     * @return span - the contents of the note
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Spanned open(String title) {

        SharedPreferences sharedPref = activity.getSharedPreferences(activity.getString(R.string.saved_notes), Context.MODE_PRIVATE);
        String json  = sharedPref.getString(activity.getString(R.string.saved_notes_key), "");
        if (json.isEmpty())
            return Html.fromHtml("");
        Gson gson = new Gson();
        Notes notes = gson.fromJson(json, Notes.class);

        Spanned span = Html.fromHtml(notes.getNotes().get(title));//, Html.FROM_HTML_MODE_LEGACY);

        return span;
    }

    /**
     * The openAll Method
     * Purpose: Get all of the notes from the map and puts them into a notes object
     * @return notes - The notes object
     */
    public Notes openAll(){
        SharedPreferences sharedPref = activity.getSharedPreferences(activity.getString(R.string.saved_notes), Context.MODE_PRIVATE);
        String json  = sharedPref.getString(activity.getString(R.string.saved_notes_key), "");
        if (json.isEmpty())
            return new Notes(new HashMap<>());
        Gson gson = new Gson();
        Notes notes = gson.fromJson(json, Notes.class);

        return notes;
    }

    /**
     * The delete Method
     * Purpose: to delete a note
     * @param title - The title of the note
     */
    public void delete(String title) {
        noteMap = openAll();

        noteMap.getNotes().remove(title);

        Gson gson = new Gson();
        String jsonString = gson.toJson(noteMap);
        SharedPreferences sharedPref = activity.getSharedPreferences(activity.getString(R.string.saved_notes), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.saved_notes_key), jsonString);
        editor.apply();
    }
}

