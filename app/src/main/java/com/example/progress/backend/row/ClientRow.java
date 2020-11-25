package com.example.progress.backend.row;

public class ClientRow {
    private int clientID;
    private String nickname;

    public ClientRow(int clientID, String nickname)
    {
        this.clientID = clientID;
        this.nickname= nickname;
    }

    public ClientRow(String nickname)
    {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return nickname;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
