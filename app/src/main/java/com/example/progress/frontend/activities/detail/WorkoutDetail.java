package com.example.progress.frontend.activities.detail;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.progress.R;
import com.example.progress.frontend.activities.HistoryActivity;
import com.example.progress.frontend.activities.ProfileActivity;
import com.example.progress.frontend.activities.NoteActivity;
import com.example.progress.frontend.activities.WorkoutActivity;
import com.example.progress.logic.Exercise;
import com.example.progress.logic.Workout;
import com.example.progress.logic.settings.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class WorkoutDetail extends AppCompatActivity {
    private Workout currentWorkout = null;
    private TextView textViewAppName = null;
    private SwipeMenuListView listViewExercise = null;
    private ArrayAdapter adapter = null;
    private TextView textViewVolume = null;
    private TextView textViewStart = null;
    private TextView textViewDuration = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        //load workout for index
        int index = getIntent().getIntExtra("workoutID",-1);
        findWorkout(index);

        Log.d("debug","OnCreate : " + currentWorkout);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //load workout for index
        int index = getIntent().getIntExtra("workoutID",-1);
        findWorkout(index);
        textViewAppName.setText(currentWorkout.getWorkoutName());

        Log.d("debug","OnResume : " + currentWorkout);
    }

    public boolean findWorkout(int id)
    {
        currentWorkout = new Workout();
        return currentWorkout.load(id);
    }

    public void startWorkoutActivity(View view) {
        if(Settings.getInstance().getCurrentClient() == null)
        {
            Log.d("debug","No client chosen");
            return;
        }
        Intent mIntent = new Intent(this, WorkoutActivity.class);
        startActivity(mIntent);
    }

    public void startProfileActivity(View view) {
        Intent mIntent = new Intent(this, ProfileActivity.class);
        mIntent.putExtra("Logged", Settings.getInstance().isClientLogged());
        startActivity(mIntent);
    }

    public void startHistoryActivity(View view){
        if(Settings.getInstance().getCurrentClient() == null)
        {
            Log.d("debug","No client chosen");
            return;
        }
        Intent mIntent = new Intent(this, HistoryActivity.class);
        startActivity(mIntent);
    }

    public void startSettingsActivity(View view) {
        Intent mIntent = new Intent(this, NoteActivity.class);
        startActivity(mIntent);
    }

    private void init()
    {
        this.textViewStart = findViewById(R.id.textView_start);
        this.textViewDuration = findViewById(R.id.textView_duration);
        this.textViewVolume = findViewById(R.id.textView_volume);
        this.textViewAppName = findViewById(R.id.text_appname);
        this.listViewExercise = findViewById(R.id.listview_workoutdetail);

        //set DateFormat
        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat sdt = new SimpleDateFormat("HH:mm:ss");
        sdt.setTimeZone(TimeZone.getTimeZone("GMT"));

        adapter = new ArrayAdapter<Exercise>(this,
                android.R.layout.simple_list_item_1,this.currentWorkout.getExerciseList());
        this.textViewVolume.setText("Volume : " + currentWorkout.getVolume() + " kg");
        this.textViewDuration.setText("Duration: " + sdt.format(new Date(currentWorkout.getDuration())));
        this.textViewStart.setText("Start: " + sdt.format(new Date(currentWorkout.getWorkoutRow().getStart())));

        listViewExercise.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
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
        listViewExercise.setMenuCreator(creator);

        listViewExercise.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        removeExercise(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        textViewAppName.setText(currentWorkout.getWorkoutName());
    }

    private void removeExercise(int position)
    {
        Exercise toDelete = this.currentWorkout.getExerciseList().get(position);
        if(!toDelete.delete())
        {
            Log.d("debug","Cant delete Exercise");
            return;
        }
        Log.d("debug",toDelete + " deleted");
        update();
    }

    private void update()
    {
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,this.currentWorkout.getExerciseList());
        listViewExercise.setAdapter(adapter);

        this.textViewVolume.setText("Volume : " + currentWorkout.getVolume() + " kg");
    }

}