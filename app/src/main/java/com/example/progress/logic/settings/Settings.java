package com.example.progress.logic.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.progress.backend.DatabaseHelper;
import com.example.progress.backend.row.ClientRow;
import com.example.progress.logic.Client;

/**
 * Singleton class
 */
public class Settings {
    private static Settings instance = null;

    private static SharedPreferences.Editor editor;
    private static final String PREFS_NAME = "SHARED PREFS ACCOUNT";
    private static final String CLIENT_KEY = "saved_client";

    /**
     * Settings constructor
     */
    private Settings(){ }

    private Client currentClient;

    private DatabaseHelper helper;

    public void saveClient(Context context)
    {
        @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CLIENT_KEY,currentClient.getClientRow().getClientID());
        editor.apply();
    }

    public void setDefaultClient(Context context)
    {
        @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_APPEND);

        try{
           int id =  sharedPreferences.getInt(CLIENT_KEY,-1);
           if(id==-1)
           {
               Log.d("debug","Cant find client");
               return;
           }

            Client client = new Client(id);
            setCurrentClient(client);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Settings instance getter
     * @return Settings static instance
     */
    public static Settings getInstance() {
        if(instance == null)
        {
            instance = new Settings();
        }
        return instance;
    }

    /**
     * Current Client getter
     * @return Client instance, if null, no Client is assigned
     */
    public Client getCurrentClient() { return currentClient;}

    /**
     * Current Client setter
     * @param client Client instance, set as Current Client
     */
    public void setCurrentClient(Client client) { this.currentClient = client;}

    /**
     *
     * @return True if Current Client is assigned, False if not
     */
    public boolean isClientLogged() {return currentClient!=null;}

    /**
     * DatabaseHelper instance getter
     * @return DatabaseHelper instance
     */
    public DatabaseHelper getHelper() { return helper; }

    /**
     * DatabaseHelper setter
     * @param helper DatabaseHelper instance, set as DatabaseHelper
     */
    public void setHelper(DatabaseHelper helper) { this.helper = helper; }
}
