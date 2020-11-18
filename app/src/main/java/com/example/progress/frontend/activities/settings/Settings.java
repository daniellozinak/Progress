package com.example.progress.frontend.activities.settings;

import com.example.progress.backend.row.ClientRow;

public class Settings {
    private static Settings instance = null;
    private Settings(){}

    private ClientRow currentClient = null;

    public static Settings getInstance() {
        if(instance == null)
        {
            instance = new Settings();
        }
        return instance;
    }

    public ClientRow getCurrentClient() { return currentClient;}

    public void setCurrentClient(ClientRow currentClient) {
        this.currentClient = currentClient;
    }
}
