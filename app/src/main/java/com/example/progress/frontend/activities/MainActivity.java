package com.example.progress.frontend.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.progress.R;
import com.example.progress.backend.DatabaseHelper;
import com.example.progress.logic.settings.Settings;

public class MainActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init Database
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        Settings.getInstance().setHelper(dbHelper);
        Settings.getInstance().setDefaultClient(this);
    }

    /**
     * Starts Workout Activity
     * @param view View instance
     */
    public void startWorkoutActivity(View view){
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
     * Starts Profile Activity
     * @param view View instance
     */
    public void startProfileActivity(View view) {
        Intent mIntent = new Intent(this,ProfileActivity.class);
        mIntent.putExtra("Logged",Settings.getInstance().isClientLogged());
        startActivity(mIntent);
    }

    /**
     * Starts History Activity
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
     * Starts Settings Activity
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
    public void textViewSwitch(View view) { }
}