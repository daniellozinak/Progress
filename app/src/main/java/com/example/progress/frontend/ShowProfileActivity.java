package com.example.progress.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.progress.R;
import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ClientRow;
import com.example.progress.backend.table.ClientTable;

import java.util.ArrayList;

public class ShowProfileActivity extends AppCompatActivity {

    private ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        listView = findViewById(R.id.profileListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("debug", "Position: " + position + " ID: " + id);
                Intent mIntent = new Intent(getApplicationContext(),ProfileActivity.class);
                mIntent.putExtra("ListPosition",position);
                startActivity(mIntent);
            }
        });

        updateListView();
    }

    void updateListView()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        ClientTable clientInstance     = ClientTable.getInstance();
        ArrayList<ClientRow> clients = clientInstance.findAllClients(dbHelper);

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,clients);
        listView.setAdapter(adapter);
    }
}