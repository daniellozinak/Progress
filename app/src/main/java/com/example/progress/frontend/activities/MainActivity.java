package com.example.progress.frontend.activities;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.progress.R;
import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.row.ExerciseRow;
import com.example.progress.backend.row.WorkoutRow;
import com.example.progress.backend.table.ClientTable;
import com.example.progress.backend.table.ExerciseTable;
import com.example.progress.backend.table.WorkoutTable;
import com.example.progress.logic.settings.Settings;
import com.example.progress.logic.Client;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private boolean isLogged = false;
    private TextView logInfo = null;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init Database
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());


        logInfo = findViewById(R.id.text_logginInfo);
        String logInfoText = (Settings.getInstance().isClientLogged())? Settings.getInstance().getCurrentClient().getClientRow().getNickname() : "";
        logInfo.setText(logInfoText);


        //debug stuff
        ClientTable clientInstance     = ClientTable.getInstance();
        WorkoutTable workoutInstance   = WorkoutTable.getInstance();
        ExerciseTable exerciseInstance = ExerciseTable.getInstance();


        Settings.getInstance().setHelper(dbHelper);
        //set helper

        ArrayList<ClientRow> clients = clientInstance.findAllClients(dbHelper);
        ArrayList<WorkoutRow> allworkouts  =  workoutInstance.findAllWorkouts(dbHelper);
        ArrayList<ExerciseRow> exercises  =  exerciseInstance.findAllExercises(dbHelper);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String logInfoText = (Settings.getInstance().isClientLogged())? Settings.getInstance().getCurrentClient().getClientRow().getNickname() : "";
        logInfo.setText(logInfoText);
    }

    public void startWorkoutActivity(View view) {
        Intent mIntent = new Intent(this,WorkoutActivity.class);
        startActivity(mIntent);
    }

    public void startProfileActivity(View view) {
        Intent mIntent = new Intent(this,ProfileActivity.class);
        mIntent.putExtra("Logged",Settings.getInstance().isClientLogged());
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