package com.example.enote;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** NotesListAdapter
 * This class is responsible for handling the list of notes
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private List<Note> testItems;
    private Context context;
    private List<Note> notesCopy;

    public NoteListAdapter(List<Note> items, Context context) {
        this.testItems = items;
        this.context = context;
        this.notesCopy = new ArrayList<>(items);
    }

    /**
     * The onCreateViewHolder Method
     * Purpose: Initializes the view holder
     * @param  parent - ViewGroup
     * @param  viewType - int
     * @return new ViewHolder(v)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new ViewHolder(v);
    }

    /**
     * The onBindViewHolder Method
     * Purpose: Sets up functionality to click on an item
     * @param  holder - ViewHolder
     * @param position - int
     */
    @Override
    public void onBindViewHolder(NoteListAdapter.ViewHolder holder, int position) {

        final Note listItem = testItems.get(position);
        holder.textViewTitle.setText(listItem.getTitle());
        holder.textViewDNote.setText(listItem.getNote());
        holder.linearLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditorActivity.class);
            intent.putExtra("TITLE_EXTRA", listItem.getTitle());
            context.startActivity(intent);
            Toast
                    .makeText(context, "Clicked on... " + listItem.getTitle(), Toast.LENGTH_LONG)
                    .show();
        });
    }

    /**
     * The getItemCount Method
     * Purpose: gets the size of the testItems list
     * @return int
     */
    @Override
    public int getItemCount() {
        return testItems.size();
    }

    /**
     * The ViewHolder Nested Class
     * Purpose: Creates custom view holder
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewTitle;
        public TextView textViewDNote;
        public LinearLayout linearLayout;

        /**
         * The ViewHolder Method
         * Purpose: The constructor for the custom class
         * @param itemView - View
         */
        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDNote =  itemView.findViewById(R.id.textViewNote);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    /**
     * The filter Method
     * Purpose: function responsible for logic of filtering notes using the search bar
     * @param title - The search parameter
     */
    // Search/Filter note list
    public void filter(String title) {
        // clear notes
        testItems.clear();

        // check for search input
        if (title.isEmpty()) {
            testItems.addAll(notesCopy);
        } else {
            // transform title to lower case for easier matching
            title = title.toLowerCase();
            for(Note n: notesCopy) {
                // add note when found
                if (n.getTitle().toLowerCase().contains(title)) {
                    testItems.add(n);
                }
            }
        }
        notifyDataSetChanged();
    }
}
