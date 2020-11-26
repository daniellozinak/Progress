package com.example.progress.frontend.activities.detail;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.progress.frontend.activities.SettingsActivity;
import com.example.progress.frontend.activities.WorkoutActivity;
import com.example.progress.logic.Exercise;
import com.example.progress.logic.Workout;
import com.example.progress.logic.settings.Settings;

public class WorkoutDetail extends AppCompatActivity {

    private TextView logInfo = null;
    private Workout currentWorkout = null;
    private TextView textViewAppName = null;
    private SwipeMenuListView listViewExercise = null;
    private ArrayAdapter adapter = null;

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
        String logInfoText = (Settings.getInstance().isClientLogged())? Settings.getInstance().getCurrentClient().getClientRow().getNickname() : "";
        logInfo.setText(logInfoText);

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
        Intent mIntent = new Intent(this, SettingsActivity.class);
        startActivity(mIntent);
    }

    private void init()
    {
        logInfo = findViewById(R.id.text_logginInfo);
        String logInfoText = (Settings.getInstance().isClientLogged())? Settings.getInstance().getCurrentClient().getClientRow().getNickname() : "";
        logInfo.setText(logInfoText);

        textViewAppName = findViewById(R.id.text_appname);
        listViewExercise = findViewById(R.id.listview_workoutdetail);
        adapter = new ArrayAdapter<Exercise>(this,
                android.R.layout.simple_list_item_1,this.currentWorkout.getExerciseList());

        listViewExercise.setAdapter(adapter);

        Log.d("debug","size of exercises: " + this.currentWorkout.getExerciseList().size());

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
        updateListView();
    }

    private void updateListView()
    {
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,this.currentWorkout.getExerciseList());
        listViewExercise.setAdapter(adapter);
    }

}