package com.example.progress.frontend.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.progress.R;
import com.example.progress.logic.Workout;
import com.example.progress.logic.settings.Settings;

public class HistoryActivity extends AppCompatActivity {

    private TextView logInfo = null;
    private ListView listViewHistory = null;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        this.init();
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
        mIntent.putExtra("Logged",true);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init()
    {
        this.listViewHistory = findViewById(R.id.listView_history);
        ArrayAdapter<Workout> workoutRowArrayAdapter = new ArrayAdapter<Workout>(this,
                android.R.layout.simple_list_item_1,Settings.getInstance().getCurrentClient().getClientFinishedWorkouts());
        this.listViewHistory.setAdapter(workoutRowArrayAdapter);

        logInfo = findViewById(R.id.text_logginInfo);
        String logInfoText = (Settings.getInstance().isClientLogged())? Settings.getInstance().getCurrentClient().getClientRow().getNickname() : "";
        logInfo.setText(logInfoText);
    }

}