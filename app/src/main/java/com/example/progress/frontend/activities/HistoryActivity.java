package com.example.progress.frontend.activities;

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
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.progress.R;
import com.example.progress.frontend.activities.detail.WorkoutDetail;
import com.example.progress.logic.Exceptions.NoClientException;
import com.example.progress.logic.Workout;
import com.example.progress.logic.settings.Settings;

public class HistoryActivity extends AppCompatActivity {

    private SwipeMenuListView listViewHistory = null;
    ArrayAdapter<Workout> workoutArrayAdapter = null;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        try {
            this.init();
        } catch (NoClientException e) {
            Toast.makeText(this,"Choose a client first",Toast.LENGTH_SHORT).show();
            Log.d("debug","Choose a client first");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Starts Workout activity
     * @param view View instance
     */
    public void startWorkoutActivity(View view)  {
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
     * Starts Profile activity
     * @param view View instance
     */
    public void startProfileActivity(View view) {
        Intent mIntent = new Intent(this,ProfileActivity.class);
        mIntent.putExtra("Logged",true);
        startActivity(mIntent);
    }

    /**
     * Starts History activity
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
     * Starts Settings activity
     * @param view View instance
     */
    public void startSettingsActivity(View view) {
        Intent mIntent = new Intent(this, NoteActivity.class);
        startActivity(mIntent);
    }

    /**
     * initialize UI elements
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() throws NoClientException {
        if(Settings.getInstance().getCurrentClient() == null) { throw new NoClientException(); }

        //set adapter for history ListView
        this.listViewHistory = findViewById(R.id.listView_history);
        this.workoutArrayAdapter = new ArrayAdapter<Workout>(this,
                android.R.layout.simple_list_item_1,Settings.getInstance().getCurrentClient().getClientFinishedWorkouts());

        this.listViewHistory.setAdapter(this.workoutArrayAdapter);

        //make a creator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem chooseItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                chooseItem.setBackground(new ColorDrawable(Color.rgb(0x85, 0x88, 0x88)));
                // set item width
                chooseItem.setWidth(180);
                //set a icon
                chooseItem.setIcon(R.drawable.ic_zoom);
                // add to menu
                menu.addMenuItem(chooseItem);

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
        listViewHistory.setMenuCreator(creator);

        listViewHistory.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        chooseWorkout(position);
                        break;
                    case 1:
                        deleteWorkout(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    /**
     * Chooses Workout at position in ListView
     * @param position
     */
    private void chooseWorkout(int position)
    {
        try{
            Workout workout = (Workout)this.workoutArrayAdapter.getItem(position);
            Intent intent = new Intent(this, WorkoutDetail.class);
            intent.putExtra("workoutID",workout.getWorkoutRow().getWorkoutID());
            startActivity(intent);
        }catch (Exception e) {}
    }

    /**
     * Deletes Workout at position
     * @param position
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deleteWorkout(int position)
    {
        if(Settings.getInstance().getCurrentClient().getClientFinishedWorkouts().size() <= position)
        {
            Toast.makeText(this,"Cant delete this Workout",Toast.LENGTH_SHORT).show();
            Log.d("debug","Out of index");
            return;
        }

        if(!Settings.getInstance().getCurrentClient().getClientFinishedWorkouts().get(position).delete())
        {
            Toast.makeText(this,"Cant delete this Workout",Toast.LENGTH_SHORT).show();
            Log.d("debug","Cant delete workout");
            return;
        }
        //refresh
        Settings.getInstance().getCurrentClient().getClientFinishedWorkouts();

        //refresh content of list

        workoutArrayAdapter = new ArrayAdapter<Workout>(getApplicationContext(),
                android.R.layout.simple_list_item_1,Settings.getInstance().getCurrentClient().getClientFinishedWorkouts());

        listViewHistory.setAdapter(workoutArrayAdapter);
        workoutArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Does nothing, purposefully
     * @param view
     */
    public void textViewSwitch(View view) {
    }
}