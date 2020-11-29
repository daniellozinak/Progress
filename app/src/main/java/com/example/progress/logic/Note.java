package com.example.progress.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Note {
    private String text;
    private Client client;
    private String id;
    private static SharedPreferences.Editor editor;
    private static final int RANDOM_LENGTH = 5;
    private static final String PREFS_NAME = "SHARED PREFS";

    public Note(String text, Client client) {
        this.text = text;
        this.client = client;
    }

    public Note(String text, Client client,String id) {
        this.text = text;
        this.client = client;
        this.id = id;
    }


    @SuppressLint("CommitPrefEdits")
    public void save(Context context) {
        @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_APPEND);

        String random = UUID.randomUUID().toString();
        id = client.getClientRow().getClientID() + "_" + random.substring(0, RANDOM_LENGTH - 1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(id, text);
        editor.apply();
    }

    public boolean delete(Context context)
    {
        @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(editor.remove(id) == null)
        {
            return false;
        }

        editor.apply();
        return true;
    }

    public static ArrayList<Note> getNotes(Client client, Context context) {
        ArrayList<Note> notes = new ArrayList<>();
        @SuppressLint("WrongConstant") Map<String, ?> keys = context.getSharedPreferences(PREFS_NAME, Context.MODE_APPEND).getAll();
        String id = Integer.toString(client.getClientRow().getClientID());
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            int index = entry.getKey().indexOf('_');
            String clientID = entry.getKey().substring(0, index);

            if (id.equals(clientID)) {
                Note newNote = new Note((String) entry.getValue(),client,entry.getKey());
                notes.add(newNote);
            }
        }

        return notes;
    }

    @Override
    public String toString() {
        return text;
    }
}
