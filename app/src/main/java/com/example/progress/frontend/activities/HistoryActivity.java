package com.example.progress.frontend.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.progress.R;
import com.example.progress.logic.Exceptions.NoClientException;
import com.example.progress.logic.Workout;
import com.example.progress.logic.settings.Settings;

import java.util.ArrayList;
import java.util.Set;

public class HistoryActivity extends AppCompatActivity {

    private TextView logInfo = null;
    private ListView listViewHistory = null;
    ArrayAdapter<Workout> workoutArrayAdapter = null;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

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

    public void startWorkoutActivity(View view)  {
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
        mIntent.putExtra("Logged",true);
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
        Intent mIntent = new Intent(this,SettingsActivity.class);
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

        this.listViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("workout array size","Length before: " + Settings.getInstance().getCurrentClient().getClientFinishedWorkouts().size());

                if(Settings.getInstance().getCurrentClient().getClientFinishedWorkouts().size() <= position)
                {
                    Log.d("debug","Out of index");
                    return;
                }

                if(!Settings.getInstance().getCurrentClient().getClientFinishedWorkouts().get(position).delete())
                {
                    Log.d("debug","Cant delete workout");
                    return;
                }
                //refresh
                Settings.getInstance().getCurrentClient().getClientFinishedWorkouts();

                Log.d("workout array size","Length after: " + Settings.getInstance().getCurrentClient().getClientFinishedWorkouts().size());

                //refresh content of list

                workoutArrayAdapter = new ArrayAdapter<Workout>(getApplicationContext(),
                        android.R.layout.simple_list_item_1,Settings.getInstance().getCurrentClient().getClientFinishedWorkouts());

                listViewHistory.setAdapter(workoutArrayAdapter);
                workoutArrayAdapter.notifyDataSetChanged();
            }
        });

        logInfo = findViewById(R.id.text_logginInfo);
        String logInfoText = (Settings.getInstance().isClientLogged())? Settings.getInstance().getCurrentClient().getClientRow().getNickname() : "";
        logInfo.setText(logInfoText);
    }


}