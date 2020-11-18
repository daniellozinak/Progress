package com.example.progress.frontend.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.progress.R;
import com.example.progress.backend.DatabaseHelper;
import com.example.progress.frontend.activities.settings.Settings;
import com.example.progress.logic.Workout;

import java.text.SimpleDateFormat;

public class WorkoutActivity extends AppCompatActivity {

    private Workout workout = null;
    private Button startButton;
    private Button endButton;
    private TextView timerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        this.workout = new Workout(new DatabaseHelper(getApplicationContext()));

        this.startButton = findViewById(R.id.button_start);
        this.endButton = findViewById(R.id.button_end);
        this.timerText = findViewById(R.id.text_timer);

        timerText.setText("Time: ");

        this.setVisibility();
    }


    public void startWorkout(View view) {
        if(Settings.getInstance().getCurrentClient() == null)
        {
            Toast.makeText(getApplicationContext(), "You need to chose profile first", Toast.LENGTH_SHORT).show();
            Log.d("debug","You need to chose profile first");
            return;
        }

        if(!this.workout.startWorkout(Settings.getInstance().getCurrentClient(), "My Workout"))
        {
            Toast.makeText(getApplicationContext(), "Can't start workout", Toast.LENGTH_SHORT).show();
            Log.d("debug","Can't start workout");
            return;
        }

        Toast.makeText(getApplicationContext(), "Workout started", Toast.LENGTH_SHORT).show();
        Log.d("debug","Workout started");
        this.runTimer();
        this.setVisibility();
    }

    public void endWorkout(View view) {
        if(Settings.getInstance().getCurrentClient() == null)
        {
            Toast.makeText(getApplicationContext(), "You need to chose profile first", Toast.LENGTH_SHORT).show();
            Log.d("debug","You need to chose profile first");
            return;
        }

        if(!this.workout.endWorkout())
        {
            Toast.makeText(getApplicationContext(), "Can't end workout", Toast.LENGTH_SHORT).show();
            Log.d("debug","Can't end workout");
            return;
        }

        Toast.makeText(getApplicationContext(), "Workout ended", Toast.LENGTH_SHORT).show();
        Log.d("debug","Workout ended");

        this.setVisibility();
    }

    private void setVisibility()
    {
        if(this.workout.isOngoingWorkout())
        {
            this.startButton.setVisibility(View.INVISIBLE);
            this.endButton.setVisibility(View.VISIBLE);
            this.timerText.setVisibility(View.VISIBLE);
            return;
        }
        this.startButton.setVisibility(View.VISIBLE);
        this.endButton.setVisibility(View.INVISIBLE);
        this.timerText.setVisibility(View.INVISIBLE);
    }
    
    private void runTimer()
    {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            long timestamp = 0;
            SimpleDateFormat sdt = new SimpleDateFormat("HH:mm:ss");
            @Override
            public void run() {
                if(workout.isOngoingWorkout())
                {
                    timestamp = workout.getCurrentTime();
                }
                timerText.setText("Time: " + sdt.format(timestamp));
                handler.postDelayed(this, 100);
            }
        });
    }
}