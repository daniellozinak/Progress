package com.example.progress.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Note {
    private final String text;
    private final Client client;
    private String key;
    private static SharedPreferences.Editor editor;
    private static final int RANDOM_LENGTH = 5;
    private static final String PREFS_NAME = "SHARED PREFS";

    /**
     * Note constructor
     * @param text Note Text
     * @param client Note Client
     */
    public Note(String text, Client client) {
        this.text = text;
        this.client = client;
    }

    /**
     * Note constructor
     * @param text Note Text
     * @param client Note Client
     * @param key Note Key
     */
    public Note(String text, Client client,String key) {
        this.text = text;
        this.client = client;
        this.key = key;
    }


    /**
     * Saves Note
     * @param context Application Context instance
     */
    @SuppressLint("CommitPrefEdits")
    public void save(Context context) {
        @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_APPEND);

        String random = UUID.randomUUID().toString();
        key = client.getClientRow().getClientID() + "_" + random.substring(0, RANDOM_LENGTH - 1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, text);
        editor.apply();
    }

    /**
     * Deletes Note
     * @param context Application Context instance
     * @return True if deleted, False if not
     */
    public boolean delete(Context context)
    {
        @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(editor.remove(key) == null)
        {
            return false;
        }

        editor.apply();
        return true;
    }

    /**
     *
     * @param client Client instance
     * @param context Application Context instance
     * @return All notes for given Client
     */
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

    /**
     * ToString method
     * @return Note Text
     */
    @Override
    public String toString() {
        return text;
    }
}
