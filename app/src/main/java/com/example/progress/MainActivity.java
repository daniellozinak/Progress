package com.example.progress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startWorkoutActivity(View view) {
        Intent mIntent = new Intent(this,WorkoutActivity.class);
        startActivity(mIntent);
    }

    public void startProfileActivity(View view) {
        Intent mIntent = new Intent(this,ProfileActivity.class);
        startActivity(mIntent);
    }

    public void startHistoryActivity(View view) {
        Intent mIntent = new Intent(this,HistoryActivity.class);
        startActivity(mIntent);
    }
}