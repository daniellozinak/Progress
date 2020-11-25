package com.example.progress.frontend.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.progress.R;
import com.example.progress.logic.Client;
import com.example.progress.logic.settings.Settings;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ArrayList<Client> clients;
    private TextView logInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        logInfo = findViewById(R.id.text_logginInfo);
        String logInfoText = (Settings.getInstance().isClientLogged())? Settings.getInstance().getCurrentClient().getClientRow().getNickname() : "";
        logInfo.setText(logInfoText);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        clients = Client.getAllClients();
        int index = getIntent().getIntExtra("ListPosition",-1);

        if(index >= 0) { Settings.getInstance().setCurrentClient(clients.get(index)); }

        String logInfoText = (Settings.getInstance().isClientLogged())? Settings.getInstance().getCurrentClient().getClientRow().getNickname() : "";
        logInfo.setText(logInfoText);
    }


    public void showProfile(View view) {
        Intent mIntent = new Intent(this,ShowProfileActivity.class);
        startActivity(mIntent);
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
}