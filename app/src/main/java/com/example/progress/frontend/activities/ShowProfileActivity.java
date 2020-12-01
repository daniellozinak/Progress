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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.progress.R;
import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.table.ClientTable;
import com.example.progress.logic.Client;
import com.example.progress.logic.settings.Settings;

import java.util.ArrayList;

public class ShowProfileActivity extends AppCompatActivity {

    private SwipeMenuListView listView = null;
    private EditText editTextProfile;
    private Button buttonCreateProfile;
    private ArrayAdapter clientAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        init();
        update();
    }

    /**
     * updates List View
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void update()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        ClientTable clientInstance     = ClientTable.getInstance();
        ArrayList<Client> clients = Client.getAllClients();

        clientAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,clients);
        listView.setAdapter(clientAdapter);
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
     * Add client to DB
     * @param view View instance
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addProfile(View view) {
        if(!this.createProfile(this.editTextProfile.getText().toString()))
        {
            Toast.makeText(this,"Cant create profile",Toast.LENGTH_SHORT).show();
            Log.d("debug","Cant create profile");
            return;
        }
        update();
    }

    /**
     * Creates Client
     * @param nickname ClientRow Nickname
     * @return True if created, False if not
     */
    private boolean createProfile(String nickname)
    {
        if(nickname.equals(""))
        {
            Toast.makeText(this,"No nickname set",Toast.LENGTH_SHORT).show();
            Log.d("debug","No nickname set");
            return false;
        }
        Client newClient = new Client(nickname);
        this.editTextProfile.setText("");
        return newClient.save();
    }

    /**
     * checks if clicked outside of EditText
     * @param event event instance
     * @return super.dispatchTouchEvent(event)
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

    /**
     * Initializes Activity Elements
     */
    private void init()
    {
        listView = findViewById(R.id.profileListView);
        this.editTextProfile = findViewById(R.id.editText_nickname);
        this.buttonCreateProfile = findViewById(R.id.button_createProfile);

        //make a creator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem chooseItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                chooseItem.setBackground(new ColorDrawable(Color.rgb(0x96, 0xF2, 0x3F)));
                // set item width
                chooseItem.setWidth(180);
                //set a icon
                chooseItem.setIcon(R.drawable.ic_save);
                // add to menu
                menu.addMenuItem(chooseItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
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
                switch (index) {
                    case 0:
                        chooseClient(position);
                        break;
                    case 1:
                        deleteClient(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    /**
     * Chooses client
     * @param position position in ListView
     */
    private void chooseClient(int position)
    {
        Intent mIntent = new Intent(getApplicationContext(),ProfileActivity.class);
        mIntent.putExtra("ListPosition",position);
        startActivity(mIntent);
    }

    /**
     * Deletes client
     * @param position position in ListView
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deleteClient(int position)
    {
        try{
            Client toDelete = (Client)clientAdapter.getItem(position);
            if(!toDelete.delete())
            {
                Toast.makeText(this,"Cant delete Client",Toast.LENGTH_SHORT).show();
                Log.d("debug","Cant delete Client");
                return;
            }
            update();
        } catch (Exception e)
        {}
    }
}