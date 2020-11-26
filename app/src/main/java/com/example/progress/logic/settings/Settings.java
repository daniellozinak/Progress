package com.example.progress.logic.settings;

import com.example.progress.backend.DatabaseHelper;
import com.example.progress.logic.Client;

/**
 * Singleton class
 */
public class Settings {
    private static Settings instance = null;

    /**
     * Settings constructor
     */
    private Settings(){ }

    private Client currentClient;

    private DatabaseHelper helper;

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
