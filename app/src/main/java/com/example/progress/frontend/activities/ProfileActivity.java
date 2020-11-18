package com.example.progress.frontend.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.progress.R;
import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.table.ClientTable;
import com.example.progress.frontend.activities.settings.Settings;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ArrayList<ClientRow> clients;
    private TextView profileTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileTextView = findViewById(R.id.text_profile);
        profileTextView.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        clients = ClientTable.getInstance().findAllClients(dbHelper);
        int index = getIntent().getIntExtra("ListPosition",-1);

        if(index >= 0)
        {
            Settings.getInstance().setCurrentClient(clients.get(index));
            profileTextView.setVisibility(View.VISIBLE);
        }

    }


    public void showProfile(View view) {
        Intent mIntent = new Intent(this,ShowProfileActivity.class);
        startActivity(mIntent);
    }
}