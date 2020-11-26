package com.example.progress.frontend.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.progress.R;
import com.example.progress.logic.Exceptions.NoClientException;
import com.example.progress.logic.settings.Settings;
import com.example.progress.logic.Exercise;
import com.example.progress.logic.ExerciseType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class WorkoutActivity extends AppCompatActivity {

    private ListView listViewExercises;
    private Button addExerciseButton;
    private Button startButton;
    private Button endButton;
    private TextView timerText;
    private TextView logInfo ;
    private Spinner exerciseTypeSpinner;
    private EditText editTextExerciseName;
    private EditText editTextReps;
    private EditText editTextWeight;
    private TextView workoutName;
    private ViewSwitcher viewSwitcherAppName;
    private EditText editTextWorkoutName;
    ArrayAdapter<Exercise> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        try {
            this.init();
        } catch (NoClientException e) {
            Log.d("debug","Choose a client first");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String logInfoText = (Settings.getInstance().isClientLogged())? Settings.getInstance().getCurrentClient().getClientRow().getNickname() : "";
        logInfo.setText(logInfoText);
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

    public void startHistoryActivity(View view) {
        if(Settings.getInstance().getCurrentClient() == null)
        {
            Log.d("debug","No client chosen");
            return;
        }
        Intent mIntent = new Intent(this,HistoryActivity.class);
        startActivity(mIntent);
    }

    public void startSettingsActivity(View view) {
        Intent mIntent = new Intent(this,SettingsActivity.class);
        startActivity(mIntent);
    }

    //TODO : check if reps are not negative number
    public void addExercise(View view) {

        ExerciseType exerciseType = ExerciseType.Core;
        String exerciseName = "";
        int reps = 0;
        int weight = 0;

        try{
            exerciseType = (ExerciseType)exerciseTypeSpinner.getSelectedItem();
            exerciseName = editTextExerciseName.getText().toString();
            reps = Integer.parseInt(editTextReps.getText().toString());
            weight = Integer.parseInt(editTextWeight.getText().toString());
        }catch(Exception e)
        {
            Log.d("debug","Cant add exercise");
            Log.d("debug",e.toString());
            return;
        }


        if(!Settings.getInstance().getCurrentClient().getCurrentWorkout().addExercise(exerciseType,exerciseName,reps,weight))
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

        //TODO add workout name
        if(!Settings.getInstance().getCurrentClient().startWorkout(System.currentTimeMillis(),"My Workout"))
        {
            Toast.makeText(getApplicationContext(), "Can't start workout", Toast.LENGTH_SHORT).show();
            Log.d("debug","Can't start workout");
            return;
        }

        //set adapter
        this.adapter = new ArrayAdapter<Exercise>(this,android.R.layout.simple_list_item_1,Settings.getInstance().getCurrentClient().getCurrentWorkout().getExerciseList());
        listViewExercises.setAdapter(adapter);

        this.workoutName.setText(Settings.getInstance().getCurrentClient().getCurrentWorkout().getWorkoutName());

        Toast.makeText(getApplicationContext(), "Workout started", Toast.LENGTH_SHORT).show();
        Log.d("debug","Workout started");
        this.runTimer();
        this.setVisibility();
    }

    @SuppressLint("SetTextI18n")
    public void endWorkout(View view) {
        if(Settings.getInstance().getCurrentClient() == null)
        {
            Toast.makeText(getApplicationContext(), "You need to chose profile first", Toast.LENGTH_SHORT).show();
            Log.d("debug","You need to chose profile first");
            return;
        }

        if(!Settings.getInstance().getCurrentClient().endWorkout())
        {
            Toast.makeText(getApplicationContext(), "Can't end workout", Toast.LENGTH_SHORT).show();
            Log.d("debug","Can't end workout");
            return;
        }


        //clear adapter and notify
        this.adapter.clear();
        this.adapter.notifyDataSetChanged();

        this.workoutName.setText("Progress");

        Toast.makeText(getApplicationContext(), "Workout ended", Toast.LENGTH_SHORT).show();
        Log.d("debug","Workout ended");

        this.setVisibility();
    }

    @SuppressLint("SetTextI18n")
    private void init() throws NoClientException {
        if(Settings.getInstance().getCurrentClient() == null) { throw new NoClientException(); }

        //init elements
        this.listViewExercises = findViewById(R.id.listview_exercises);
        this.addExerciseButton = findViewById(R.id.button_addExercise);
        this.startButton = findViewById(R.id.button_start);
        this.endButton = findViewById(R.id.button_end);
        this.timerText = findViewById(R.id.text_timer);
        this.listViewExercises = findViewById(R.id.listview_exercises);
        this.editTextExerciseName = findViewById(R.id.editText_exerciseName);
        this.editTextReps = findViewById(R.id.editText_reps);
        this.editTextWeight = findViewById(R.id.editText_weight);
        this.workoutName = findViewById(R.id.text_appname);
        this.viewSwitcherAppName = findViewById(R.id.switcher_appname);
        this.editTextWorkoutName = findViewById(R.id.editText_hidden_appname);


        //set adapter for exercise spinner
        this.exerciseTypeSpinner = findViewById(R.id.spinner_exerciseType);
        ExerciseType[] exerciseTypeArray = new ExerciseType[]{ExerciseType.Core,
                ExerciseType.Arms,ExerciseType.Back,ExerciseType.Chest,ExerciseType.Legs,ExerciseType.Shoulders};
        ArrayAdapter<ExerciseType> exerciseTypeAdapter = new ArrayAdapter<ExerciseType>(this, android.R.layout.simple_spinner_dropdown_item,exerciseTypeArray);
        exerciseTypeSpinner.setAdapter(exerciseTypeAdapter);

        timerText.setText("Time: ");


        //global info
        logInfo = findViewById(R.id.text_logginInfo);
        String logInfoText = (Settings.getInstance().isClientLogged())? Settings.getInstance().getCurrentClient().getClientRow().getNickname() : "";
        logInfo.setText(logInfoText);


        //TODO 2: Add Gestures
        listViewExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Settings.getInstance().getCurrentClient().getCurrentWorkout().removeExercise(position);
                adapter.notifyDataSetChanged();
            }
        });

        this.setVisibility();
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
                clickOutSide();
                hideKeyboard(this);
            }

        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Triggered by clicking on TextView workoutName
     * @param view View instance
     */
    public void textViewSwitch(View view)
    {
        if(!Settings.getInstance().getCurrentClient().isCurrentWorkout()) {return;}
        //show EditText
        this.viewSwitcherAppName.showNext();
    }

    /**
     * sets Visibility for WorkoutActivity
     */
    private void setVisibility()
    {
        if(Settings.getInstance().getCurrentClient().isCurrentWorkout())
        {
            this.addExerciseButton.setVisibility(View.VISIBLE);
            this.startButton.setVisibility(View.INVISIBLE);
            this.endButton.setVisibility(View.VISIBLE);
            this.timerText.setVisibility(View.VISIBLE);
            this.listViewExercises.setVisibility(View.VISIBLE);
            this.editTextExerciseName.setVisibility(View.VISIBLE);
            this.exerciseTypeSpinner.setVisibility(View.VISIBLE);
            this.editTextReps.setVisibility(View.VISIBLE);
            this.editTextWeight.setVisibility(View.VISIBLE);
            return;
        }
        this.addExerciseButton.setVisibility(View.INVISIBLE);
        this.startButton.setVisibility(View.VISIBLE);
        this.endButton.setVisibility(View.INVISIBLE);
        this.timerText.setVisibility(View.INVISIBLE);
        this.listViewExercises.setVisibility(View.INVISIBLE);
        this.editTextExerciseName.setVisibility(View.INVISIBLE);
        this.exerciseTypeSpinner.setVisibility(View.INVISIBLE);
        this.editTextReps.setVisibility(View.INVISIBLE);
        this.editTextWeight.setVisibility(View.INVISIBLE);
    }
    
    private void runTimer()
    {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            long timestamp = 0;
            @SuppressLint("SimpleDateFormat")
            final SimpleDateFormat sdt = new SimpleDateFormat("HH:mm:ss");
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if(Settings.getInstance().getCurrentClient().isCurrentWorkout())
                {
                    timestamp = Settings.getInstance().getCurrentClient().getCurrentWorkout().getCurrentTime();
                }
                sdt.setTimeZone(TimeZone.getTimeZone("GMT"));
                timerText.setText("Time: " + sdt.format(new Date(timestamp)));
                handler.postDelayed(this, 100);
            }
        });
    }

    /**
     * Triggered by clicking outside of EditText editTextWorkoutName
     * TODO: Toast
     */
    private void clickOutSide()
    {
        // if no input given, returns
        if(editTextWorkoutName.getText().equals(""))
        {
            Log.d("debug","Workout Name not set");
            return;
        }


        String newName = editTextWorkoutName.getText().toString();
        //if newName is the same as CurrentWorkout Name, return
        if(Settings.getInstance().getCurrentClient().getCurrentWorkout().getWorkoutRow().getName().equals(newName)) {return;}

        //sets Name to CurrentWorkout and TextView as AppName
        Settings.getInstance().getCurrentClient().getCurrentWorkout().getWorkoutRow().setName(newName);
        this.workoutName.setText(newName);
        //show TextView
        this.viewSwitcherAppName.showPrevious();
        Log.d("debug","Workout Name set: " + editTextWorkoutName.getText());
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