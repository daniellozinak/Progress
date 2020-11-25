package com.example.progress.logic.settings;

import com.example.progress.backend.DatabaseHelper;
import com.example.progress.logic.Client;
import com.example.progress.logic.Workout;

public class Settings {
    private static Settings instance = null;

    private Settings(){ }

    private Client currentClient;

    private DatabaseHelper helper;

    public static Settings getInstance() {
        if(instance == null)
        {
            instance = new Settings();
        }
        return instance;
    }

    public Client getCurrentClient() { return currentClient;}

    public void setCurrentClient(Client client) { this.currentClient = client;}

    public boolean isClientLogged() {return currentClient!=null;}

    public DatabaseHelper getHelper() { return helper; }

    public void setHelper(DatabaseHelper helper) { this.helper = helper; }
}
