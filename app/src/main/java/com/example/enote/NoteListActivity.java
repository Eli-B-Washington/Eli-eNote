package com.example.enote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.enote.ui.main.FileHelper;
import com.example.enote.ui.main.Notes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** NoteListActivity
 * This class is the activity responsible for displaying the list of notes
 */
public class NoteListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<Note> noteList;


    /**
     * The onCreate Method
     * Purpose: Loads the list of notes
     * @param  savedInstanceState - a Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        recyclerView = findViewById(R.id.notesList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FileHelper fileHelper = new FileHelper(this);
        Notes notes = fileHelper.openAll();

        noteList = new ArrayList<>();
        for (Map.Entry<String, String> entry : notes.getNotes().entrySet()) {
            Note note = new Note();
            note.setTitle(entry.getKey());
            note.setNote((Spannable) Html.fromHtml(entry.getValue()));
            noteList.add(note);
        }

        adapter = new NoteListAdapter(noteList, this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * The editNewNote Method
     * Purpose: to create a new note and open the editor activity
     * @param  view - Allows a button to find this function
     */
    public void editNewNote(View view) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("TITLE_EXTRA", "");
        startActivity(intent);
    }

    /**
     * The onCreateOptionsMenu Method
     * Purpose:  Creates the Search Bar
     * @param menu - Necessary for the function
     * @return a true or false boolean value
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        NoteListAdapter a = new NoteListAdapter(noteList, this);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        // Implement inside new instance instead of overriding each method
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                a.filter(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                a.filter(newText);
                return true;
            }
        });
        recyclerView.setAdapter(a);
        return true;
    }
}

