package com.example.progress.frontend.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.progress.R;
import com.example.progress.logic.Note;
import com.example.progress.logic.settings.Settings;

public class NoteActivity extends AppCompatActivity {

    private SwipeMenuListView listView;
    private ArrayAdapter<Note> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        init();
    }

    /**
     * Initializes Activity element
     */
    private void init() {
        listView = findViewById(R.id.listView_notes);

        if(!Settings.getInstance().isClientLogged()) { return; }

        adapter = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1,
                Note.getNotes(Settings.getInstance().getCurrentClient(),this));
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(180);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    Note toDelete = adapter.getItem(position);
                   if(!toDelete.delete(getApplicationContext()))
                   {
                       Toast.makeText(getApplicationContext(),"Cant delete note",Toast.LENGTH_SHORT).show();
                       Log.d("debug","Cant delete note");
                       return false;
                   }
                   update();
                   return true;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    /**
     * Updates ListView
     */
    private void update()
    {
        adapter = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1,
                Note.getNotes(Settings.getInstance().getCurrentClient(),this));
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Settings.getInstance().isClientLogged()) { return; }

        init();
    }

    /**
     * Starts Workout activity
     * @param view View instance
     */
    public void startWorkoutActivity(View view) {
        if(Settings.getInstance().getCurrentClient() == null)
        {
            Toast.makeText(this,"No client chosen",Toast.LENGTH_SHORT).show();
            Log.d("debug","No client chosen");
            return;
        }
        Intent mIntent = new Intent(this,WorkoutActivity.class);
        startActivity(mIntent);
    }

    /**
     * Starts Profile activity
     * @param view View instance
     */
    public void startProfileActivity(View view) {
        Intent mIntent = new Intent(this,ProfileActivity.class);
        mIntent.putExtra("Logged", Settings.getInstance().isClientLogged());
        startActivity(mIntent);
    }

    /**
     * Starts History activity
     * @param view View instance
     */
    public void startHistoryActivity(View view){
        if(Settings.getInstance().getCurrentClient() == null)
        {
            Toast.makeText(this,"No client chosen",Toast.LENGTH_SHORT).show();
            Log.d("debug","No client chosen");
            return;
        }
        Intent mIntent = new Intent(this,HistoryActivity.class);
        startActivity(mIntent);
    }

    /**
     * Starts Settings activity
     * @param view View instance
     */
    public void startSettingsActivity(View view) {
        Intent mIntent = new Intent(this, NoteActivity.class);
        startActivity(mIntent);
    }

    /**
     * Does nothing, purposefully
     * @param view
     */
    public void textViewSwitch(View view) {
    }

    /**
     * Adds note to SharePreferences
     * @param view View instance
     */
    public void addNote(View view) {
        if(!Settings.getInstance().isClientLogged())
        {
            Toast.makeText(this,"No client logged",Toast.LENGTH_SHORT).show();
            Log.d("debug","No client logged");
            return;
        }
        EditText noteText = findViewById(R.id.editText_Note);
        String text = noteText.getText().toString();

        if(text.equals(""))
        {
            Toast.makeText(this,"Cant add empty string",Toast.LENGTH_SHORT).show();
            Log.d("debug","Cant add empty string");
            return;
        }

        Note note = new Note(text,Settings.getInstance().getCurrentClient());
        note.save(this);

        update();
    }

    /**
     * If clicked outside of EdiText, hides keyboard
     * @param event Event instance
     * @return dispatchTouchEvent(event)
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();

        if (v != null && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int[] sourceCoordinates = new int[2];
            v.getLocationOnScreen(sourceCoordinates);
            float x = event.getRawX() + v.getLeft() - sourceCoordinates[0];
            float y = event.getRawY() + v.getTop() - sourceCoordinates[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                hideKeyboard(this);
            }

        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Hides Keyboard
     * @param activity Activity instance
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            activity.getWindow().getDecorView();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    }
}

