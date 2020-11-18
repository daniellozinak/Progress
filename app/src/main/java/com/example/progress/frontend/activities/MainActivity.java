package com.example.progress.frontend.activities;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.progress.R;
import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.row.ExerciseRow;
import com.example.progress.backend.row.WorkoutRow;
import com.example.progress.backend.table.ClientTable;
import com.example.progress.backend.table.ExerciseTable;
import com.example.progress.backend.table.WorkoutTable;
import com.example.progress.frontend.activities.settings.Settings;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private boolean isLogged = false;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init Database
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());


        ClientTable clientInstance     = ClientTable.getInstance();
        WorkoutTable workoutInstance   = WorkoutTable.getInstance();
        ExerciseTable exerciseInstance = ExerciseTable.getInstance();

        ClientRow test_client = clientInstance.findClient(23,dbHelper);
        Settings.getInstance().setCurrentClient(test_client);

        workoutInstance.deleteClientWorkout(test_client,dbHelper);

        ArrayList<ClientRow> clients = clientInstance.findAllClients(dbHelper);
        ArrayList<WorkoutRow> allworkouts  =  workoutInstance.findAllWorkouts(dbHelper);
        ArrayList<ExerciseRow> exercises  =  exerciseInstance.findAllExercises(dbHelper);

    }

    public void startWorkoutActivity(View view) {
        Intent mIntent = new Intent(this,WorkoutActivity.class);
        startActivity(mIntent);
    }

    public void startProfileActivity(View view) {
        Intent mIntent = new Intent(this,ProfileActivity.class);
        mIntent.putExtra("Logged",isLogged);
        startActivity(mIntent);
    }

    public void startHistoryActivity(View view) {
        Intent mIntent = new Intent(this,HistoryActivity.class);
        startActivity(mIntent);
    }

    public void startSettingsActivity(View view) {
        Intent mIntent = new Intent(this,SettingsActivity.class);
        startActivity(mIntent);
    }
}