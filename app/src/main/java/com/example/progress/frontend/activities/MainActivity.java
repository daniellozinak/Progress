package com.example.progress.frontend.activities;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.progress.R;
import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.row.ExerciseRow;
import com.example.progress.backend.row.WorkoutRow;
import com.example.progress.backend.table.ClientTable;
import com.example.progress.backend.table.ExerciseTable;
import com.example.progress.backend.table.WorkoutTable;
import com.example.progress.logic.settings.Settings;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init Database
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());


        //debug stuff
        ClientTable clientInstance     = ClientTable.getInstance();
        WorkoutTable workoutInstance   = WorkoutTable.getInstance();
        ExerciseTable exerciseInstance = ExerciseTable.getInstance();

        Settings.getInstance().setHelper(dbHelper);
        Settings.getInstance().setDefaultClient(this);
        //set helper

        ArrayList<ClientRow> clients = clientInstance.findAllClients(dbHelper);
        ArrayList<WorkoutRow> allworkouts  =  workoutInstance.findAllWorkouts(dbHelper);
        ArrayList<ExerciseRow> exercises  =  exerciseInstance.findAllExercises(dbHelper);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void startWorkoutActivity(View view){
        if(Settings.getInstance().getCurrentClient() == null)
        {
            Log.d("debug","No client chosen");
            return;
        }
        Intent mIntent = new Intent(this,WorkoutActivity.class);
        startActivity(mIntent);
    }

    public void startProfileActivity(View view) {
        Intent mIntent = new Intent(this,ProfileActivity.class);
        mIntent.putExtra("Logged",Settings.getInstance().isClientLogged());
        startActivity(mIntent);
    }

    public void startHistoryActivity(View view){
        if(Settings.getInstance().getCurrentClient() == null)
        {
            Log.d("debug","No client chosen");
            return;
        }
        Intent mIntent = new Intent(this,HistoryActivity.class);
        startActivity(mIntent);
    }

    public void startSettingsActivity(View view) {
        Intent mIntent = new Intent(this, NoteActivity.class);
        startActivity(mIntent);
    }

    public void textViewSwitch(View view) { }
}