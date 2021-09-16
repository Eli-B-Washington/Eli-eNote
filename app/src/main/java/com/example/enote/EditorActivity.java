package com.example.enote;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.enote.ui.main.FileHelper;
import com.example.enote.ui.main.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;
import static android.graphics.Typeface.NORMAL;

/** Editor Activity
 * This class is the activity responsible for the editing of notes.
 */
public class EditorActivity extends AppCompatActivity {

    // made variable names descriptive
    private EditText noteContent;
    private EditText noteTitle;
    private String title;
    private boolean newNote;
    private boolean deleted = false;

    FileHelper fileHelper;

    /**
     * The onCreate Method
     * Purpose: Creates a new note or loads an existing note.
     * @param savedInstanceState - bundle
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        noteTitle = (EditText) findViewById(R.id.editNoteTitle);
        noteContent = findViewById(R.id.editNoteContent);//EF
        fileHelper = new FileHelper(this);

        //Sets up the functionality of the save button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(view -> {
            //this is what happens when you click on the save button
            Spannable text = new SpannableStringBuilder(noteContent.getText());
            //check to see if either of the textfields are empty
            if (!noteTitle.getText().toString().isEmpty() &&
                    !noteContent.getText().toString().isEmpty()) {
                Notes notes = fileHelper.openAll();
                //check for duplicates
                if (!notes.getNotes().containsKey(noteTitle.getText().toString()) || !newNote ||
                        title.equals(noteTitle.getText().toString())) {
                    fileHelper.save(noteTitle.getText().toString(), text);
                    noteTitle.setEnabled(false);
                    //check to see if title has changed
                    if (!title.equals(noteTitle.getText().toString()))
                    {
                        fileHelper.delete(title);
                    }
                    title = noteTitle.getText().toString();
                    Toast.makeText(getApplicationContext(), "Saved " + noteTitle.getText().toString() + ".", Toast.LENGTH_SHORT).show();
                } else {
                    noteTitle.setError("Can't have duplicate titles.");
                }
            } else {
                // Notify user when a field is empty
                if (noteTitle.getText().toString().isEmpty()) {
                    noteTitle.setError("This field is required.");
                }

                if (noteContent.getText().toString().isEmpty()) {
                    noteContent.setError("This field is required.");
                }
            }
        });

        //This loads a note if it is an existing note or sets up the environment for a new note.
        Intent intent = getIntent();
        title = intent.getStringExtra("TITLE_EXTRA");
        if (!title.isEmpty()) {
            noteTitle.setEnabled(false);
            noteTitle.setText(title);
            noteContent.setText(fileHelper.open(title));
            noteContent.requestFocus();
            newNote = false;
            getSupportActionBar().setTitle("Edit Note");
        } else {
            newNote = true;
            noteTitle.requestFocus();
            getSupportActionBar().setTitle("Create Note");
        }
    }

    /**
     * The onStop Method
     * Purpose: Saves a note in a thread while closing it.
     * @return nothing
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onStop() {
        super.onStop();
        if (deleted) {
            return;
        }

        // check to see if fields are empty
        if (noteTitle.getText().toString().isEmpty() ||
                noteContent.getText().toString().isEmpty()) {
            return;
        }

        Runnable runnable = () -> {
            try {
                Notes notes = fileHelper.openAll();

                if (notes.getNotes().containsKey(noteTitle.getText().toString()) && newNote)
                    return;
                Spannable text = new SpannableStringBuilder(noteContent.getText());
                fileHelper.save(noteTitle.getText().toString(), text);
            } catch (Throwable t) {}
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * The Bold Method
     * Purpose: to bold highlighted text in a note
     * @param  view - allows a button to find this function
     */
    //Formatting
    public void Bold(View view) {
        Spannable spannableString = new SpannableStringBuilder(noteContent.getText());
        spannableString.setSpan(new StyleSpan(BOLD),
                noteContent.getSelectionStart(),
                noteContent.getSelectionEnd(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        noteContent.setText(spannableString);
    }

    /**
     * The Italics Method
     * Purpose: to italicize highlighted text in a note
     * @param view - allows a button to find this function
     */
    public void Italics(View view) {
        Spannable spannableString = new SpannableStringBuilder(noteContent.getText());
        spannableString.setSpan(new StyleSpan(ITALIC),
                noteContent.getSelectionStart(),
                noteContent.getSelectionEnd(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        noteContent.setText(spannableString);
    }
    /**
     * The Underline Method
     * Purpose: to underline highlighted text in a note
     * @param view - allows a button to find this function
     */
    public void Underline(View view) {
        Spannable spannableString = new SpannableStringBuilder(noteContent.getText());
        spannableString.setSpan(new UnderlineSpan(),
                noteContent.getSelectionStart(),
                noteContent.getSelectionEnd(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        noteContent.setText(spannableString);
    }

    /**
     * The Clear Formatting Method
     * Purpose: to return the formatting of the highlighted text to normal
     * @param view - allows a button to find this function
     */
    public void ClearFormatting(View view) {
        //removes bold an italics
        Spannable spannable = new SpannableStringBuilder(noteContent.getText());
        StyleSpan[] ss = spannable.getSpans(noteContent.getSelectionStart(), noteContent.getSelectionEnd(), StyleSpan.class);
        for (int i = 0; i < ss.length; i++) {
            if (ss[i].getStyle() != NORMAL) {
                spannable.removeSpan(ss[i]);
            }
        }
        //remove underline
        spannable.setSpan(new NoUnderlineSpan(),
                noteContent.getSelectionStart(),
                noteContent.getSelectionEnd(),
                0);

        noteContent.setText(spannable);
    }

    /**
     * The onCreateOptionsMenu Method
     * Purpose: Creates the menu
     * @param  menu - a menu that has the options to email, delete, and save the note to a PDF
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /**
     * The onOptionsItemsSelected Method
     * Purpose: Determines what happens when a menu item is selected
     * @param  item - The menu item selected
     * @return super.onOptionsItemSelected(item);
     */
    // handle button activities
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String SUBJECT = noteTitle.getText().toString();
        String BODY = noteContent.getText().toString();

        /*Converts the note to text that can be put in an email and brings up the pop up menu
         *for selecting how to email the note.
         */
        if (id == R.id.email) {
            String[] RECIPIENT = {""}; // This should be a variable

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, RECIPIENT);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
            emailIntent.putExtra(Intent.EXTRA_TEXT, BODY);
            emailIntent.setType("message/rfc822");
            startActivity(Intent.createChooser(emailIntent, "Choose Mail App"));
        } else if (id == R.id.delete) {
            deleted = true;
            fileHelper.delete(noteTitle.getText().toString());
            Intent intent = new Intent(this, NoteListActivity.class);
            startActivity(intent);
        } else if (id == R.id.savePDF) {
            // save note as PDF
            createPdf(BODY, SUBJECT);
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * The createPDF Method
     * Purpose: to create a PDF version of the note
     * @param content - String
     * @param fileTitle - String
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdf(String content, String fileTitle){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        // set text color to black
        paint.setColor(Color.BLACK);
        // add title
        canvas.drawText(fileTitle, 10, 50, paint);
        // add content
        canvas.drawText(content, 10, 70, paint);
        // finish the page
        document.finishPage(page);

        // set directory
        // TODO: Ask user to allow eNote to have access to internal storage
        // TODO: Add new line to content, content currently saved in one line
        // Manually grant eNote app permission to storage
        // Go to Settings > App > eNote > Turn on permission to storage
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/eNotePDF/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + fileTitle + ".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, fileTitle +  " PDF Created.", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }
}
