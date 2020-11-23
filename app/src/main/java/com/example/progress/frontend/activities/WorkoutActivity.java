package com.example.progress.frontend.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.progress.R;
import com.example.progress.backend.DatabaseHelper;
import com.example.progress.frontend.activities.settings.Settings;
import com.example.progress.logic.Exercise;
import com.example.progress.logic.ExerciseType;
import com.example.progress.logic.Workout;

import java.text.SimpleDateFormat;

public class WorkoutActivity extends AppCompatActivity {

    private Workout workout = null;
    private ListView listViewExercises = null;
    private Button addExerciseButton;
    private Button startButton;
    private Button endButton;
    private TextView timerText;
    private TextView logInfo = null;
    private Spinner exerciseTypeSpinner = null;
    ArrayAdapter<Exercise> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        this.init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String logInfoText = (Settings.getInstance().isClientLogged())? Settings.getInstance().getCurrentClient().getNickname() : "";
        logInfo.setText(logInfoText);
    }

    private void init()
    {
        this.workout = new Workout(new DatabaseHelper(getApplicationContext()));

        //init elements
        this.listViewExercises = findViewById(R.id.listview_exercises);
        this.addExerciseButton = findViewById(R.id.button_addExercise);
        this.startButton = findViewById(R.id.button_start);
        this.endButton = findViewById(R.id.button_end);
        this.timerText = findViewById(R.id.text_timer);
        this.listViewExercises = findViewById(R.id.listview_exercises);

        //TODO 1 Add options
        this.exerciseTypeSpinner = findViewById(R.id.spinner_exerciseType);

        timerText.setText("Time: ");


        //global info
        logInfo = findViewById(R.id.text_logginInfo);
        String logInfoText = (Settings.getInstance().isClientLogged())? Settings.getInstance().getCurrentClient().getNickname() : "";
        logInfo.setText(logInfoText);


        //set adapter
        this.adapter = new ArrayAdapter<Exercise>(this,android.R.layout.simple_list_item_1,workout.getExerciseList());
        listViewExercises.setAdapter(adapter);


        //TODO 2: Add Gestures
        listViewExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                workout.removeExercise(position);
                adapter.notifyDataSetChanged();
            }
        });

        this.setVisibility();
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

    public void addExercise(View view) {
        if(!this.workout.addExercise(ExerciseType.Arms,"Biceps Curl",12))
        {
            Log.d("debug","Can't add exercise");
            return;
        }
        this.adapter.notifyDataSetChanged();
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


        //clear adapter and notify
        this.adapter.clear();
        this.adapter.notifyDataSetChanged();

        Toast.makeText(getApplicationContext(), "Workout ended", Toast.LENGTH_SHORT).show();
        Log.d("debug","Workout ended");

        this.setVisibility();
    }

    private void setVisibility()
    {
        if(this.workout.isOngoingWorkout())
        {
            this.addExerciseButton.setVisibility(View.VISIBLE);
            this.startButton.setVisibility(View.INVISIBLE);
            this.endButton.setVisibility(View.VISIBLE);
            this.timerText.setVisibility(View.VISIBLE);
            this.listViewExercises.setVisibility(View.VISIBLE);
            return;
        }
        this.addExerciseButton.setVisibility(View.INVISIBLE);
        this.startButton.setVisibility(View.VISIBLE);
        this.endButton.setVisibility(View.INVISIBLE);
        this.timerText.setVisibility(View.INVISIBLE);
        this.listViewExercises.setVisibility(View.INVISIBLE);
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