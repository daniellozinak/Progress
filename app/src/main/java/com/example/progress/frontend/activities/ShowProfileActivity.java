package com.example.progress.frontend.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.progress.R;
import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.table.ClientTable;
import com.example.progress.logic.Client;

import java.util.ArrayList;

public class ShowProfileActivity extends AppCompatActivity {

    private ListView listView = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        listView = findViewById(R.id.profileListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(getApplicationContext(),ProfileActivity.class);
                mIntent.putExtra("ListPosition",position);
                startActivity(mIntent);
            }
        });

        updateListView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void updateListView()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        ClientTable clientInstance     = ClientTable.getInstance();
        ArrayList<Client> clients = Client.getAllClients();

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,clients);
        listView.setAdapter(adapter);
    }
}