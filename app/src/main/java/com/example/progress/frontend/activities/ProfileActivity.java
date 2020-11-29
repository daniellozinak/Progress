package com.example.progress.frontend.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.progress.R;
import com.example.progress.logic.Client;
import com.example.progress.logic.Workout;
import com.example.progress.logic.settings.Settings;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {

    private ArrayList<Client> clients;
    private TextView textViewLogged;
    private GraphView volumeGraph,frequencyGraph;

    private static final int WORKOUT_LIMIT = 10;
    private static final int DAY_LIMIT = 5*7 - 1;
    private static final int MILLISECONDS_PER_DAY = 86400000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.init();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        clients = Client.getAllClients();
        int index = getIntent().getIntExtra("ListPosition",-1);

        if(index >= 0)
        {
            Settings.getInstance().setCurrentClient(clients.get(index));
            Settings.getInstance().saveClient(this);
        }
        setVisibility();
        setVolumeGraph();
        setFrequencyGraph();
    }


    public void showProfile(View view) {
        Intent mIntent = new Intent(this,ShowProfileActivity.class);
        startActivity(mIntent);
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
        Intent mIntent = new Intent(this, NoteActivity.class);
        startActivity(mIntent);
    }

    private void init()
    {
        this.textViewLogged = findViewById(R.id.textView_logged);
        this.volumeGraph = findViewById(R.id.graph_volume);
        this.frequencyGraph = findViewById(R.id.graph_frequency);

        volumeGraph.getGridLabelRenderer().setPadding(40);
        frequencyGraph.getGridLabelRenderer().setPadding(40);

        this.setVisibility();
    }

    private void setVisibility()
    {
        if(Settings.getInstance().isClientLogged())
        {
            textViewLogged.setText("Logged as " + Settings.getInstance().getCurrentClient().getClientRow().getNickname());
            textViewLogged.setVisibility(View.VISIBLE);
            return;
        }
        textViewLogged.setText("No logged profile");
        textViewLogged.setVisibility(View.INVISIBLE);
    }

    public void textViewSwitch(View view) { }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setVolumeGraph()
    {
        if(!Settings.getInstance().isClientLogged())
        {
            Log.d("debug","Client not logged");
            return;
        }


        BarGraphSeries<DataPoint> seriesVolume = new BarGraphSeries<>();
        int ClientWorkoutSize = Settings.getInstance().getCurrentClient().getClientFinishedWorkouts().size() - 1;

        for(int i = 0; i <= WORKOUT_LIMIT; i ++)
        {
            int volume = 0;
            if(i <= ClientWorkoutSize)
            {
                Workout workout = Settings.getInstance().getCurrentClient().getClientFinishedWorkouts().get(ClientWorkoutSize - i);
                volume = workout.getVolume();
            }
            seriesVolume.appendData(new DataPoint(i,volume),true,60);
        }


        seriesVolume.setSpacing(50);
        seriesVolume.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb(200, 50, 100);
            }
        });

        volumeGraph.getGridLabelRenderer().setNumHorizontalLabels(ClientWorkoutSize);
        volumeGraph.getGridLabelRenderer().setTextSize(25f);
        volumeGraph.getGridLabelRenderer().reloadStyles();
        volumeGraph.getViewport().setMinX(0);
        volumeGraph.getViewport().setMaxX(WORKOUT_LIMIT);
        volumeGraph.getViewport().setXAxisBoundsManual(true);


        volumeGraph.addSeries(seriesVolume);
        volumeGraph.getGridLabelRenderer().setVerticalAxisTitle("Volume in KG");
        volumeGraph.getGridLabelRenderer().setHorizontalAxisTitle("Last " + WORKOUT_LIMIT + " Workouts");
        volumeGraph.setTitle("Workout Volume Graph");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setFrequencyGraph()
    {
        if(!Settings.getInstance().isClientLogged())
        {
            Log.d("debug","Client not logged");
            return;
        }

        BarGraphSeries<DataPoint> seriesFrequency = new BarGraphSeries<>();
        Calendar calendarMax = daysTillSunday();
        Calendar calendarMin = Calendar.getInstance();

        Date maxDate = calendarMax.getTime();
        calendarMin.setTime(maxDate);
        calendarMin.add(Calendar.DATE,-1 * DAY_LIMIT);
        Date minDate = calendarMin.getTime();

        for(int i = 0; i < 5 ; i ++)
        {
            int dayInWeek = calendarMin.get(Calendar.DAY_OF_WEEK);
            int workoutsInWeek = 0;
            while(dayInWeek != 1)
            {
                for(Workout workout : Settings.getInstance().getCurrentClient().getClientFinishedWorkouts())
                {
                    if(this.isSameDay(new Date(workout.getWorkoutRow().getStart()),calendarMin.getTime()))
                    {
                        workoutsInWeek++;
                    }
                }

                //add one day
                calendarMin.add(Calendar.DATE,1);
                dayInWeek = calendarMin.get(Calendar.DAY_OF_WEEK);
            }
            seriesFrequency.appendData(new DataPoint(calendarMin.getTime(),workoutsInWeek),false,5);
            calendarMin.add(Calendar.DATE,1);
        }

        frequencyGraph.getGridLabelRenderer().setNumHorizontalLabels(5);
        frequencyGraph.getGridLabelRenderer().setTextSize(25f);
        frequencyGraph.getGridLabelRenderer().reloadStyles();

        // set manual x bounds
        frequencyGraph.getViewport().setMinX(minDate.getTime());
        frequencyGraph.getViewport().setMaxX(maxDate.getTime());
        frequencyGraph.getViewport().setMinY(0);
        frequencyGraph.getViewport().setMaxY(7);
        frequencyGraph.getViewport().setXAxisBoundsManual(true);

        seriesFrequency.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return setColor((int) data.getY(),7.0);
            }
        });

        //define my own label formatter
        frequencyGraph.getGridLabelRenderer().setLabelFormatter(new LabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                //if x value is passed
                if(isValueX)
                {
                    //convert long to timestamp
                    long timestamp = (long)value;
                    // create date format
                    @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdt = new SimpleDateFormat("MMM/dd/yy");
                    //return formatted date
                    return sdt.format(new Date(timestamp));
                }
                //if y value is passed, convert to int, then to String
                return Integer.toString((int)value);
            }

            @Override
            public void setViewport(Viewport viewport) {
            }
        });

        frequencyGraph.getGridLabelRenderer().setHumanRounding(false);
        seriesFrequency.setSpacing(5);

        frequencyGraph.getGridLabelRenderer().setVerticalAxisTitle("Workouts per Week");
        frequencyGraph.addSeries(seriesFrequency);
        frequencyGraph.setTitle("Workout Frequency Graph");
    }

    private Calendar daysTillSunday()
    {
        //set today's date
        Date myDate = new Date();
        myDate.setTime(myDate.getTime());

        //set calendar to today's date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);

        //calculate day of the week
        int dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //while day is not sunday (sunday = 1, saturday = 7)
        while(dayOfTheWeek != 1)
        {
            //add one day
            calendar.add(Calendar.DATE,1);
            dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
        }

        return calendar;
    }

    private boolean isSameDay(Date date1, Date date2)
    {
        long day1 = date1.getTime() / MILLISECONDS_PER_DAY;
        long day2 = date2.getTime() / MILLISECONDS_PER_DAY;

        return day1 == day2;
    }

    private int setColor(int data,double max)
    {
        if(data <= 0) { return 0;}
        double divider = (double)data/max;
        int green = (int)(divider * 255.0);
        //Color.rgb(255 - (data/7 * 255),(data/7 * 255),0)
        if(data > max)
        {
            green = 255;
        }
        return  Color.rgb(255 - green ,green,0);
    }
}