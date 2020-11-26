package com.example.progress.backend.row;

public class ClientRow {
    private int clientID;
    private String nickname;

    /**
     * ClientRow constructor
     * @param clientID set as Client ID
     * @param nickname set as Client Nickname
     */
    public ClientRow(int clientID, String nickname)
    {
        this.clientID = clientID;
        this.nickname= nickname;
    }

    /**
     * ClientRow constructor
     * @param nickname set as Client Nickname
     */
    public ClientRow(String nickname)
    {
        this.nickname = nickname;
    }

    /**
     * ClientRow ToString
     * @return  Client Nickname
     */
    @Override
    public String toString() {
        return nickname;
    }

    /**
     * ClientRow ID getter
     * @return  Client ID
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * ClientRow ID setter
     * @param clientID set as Client ID
     */
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    /**
     * ClientRow Nickname getter
     * @return  Client Nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * ClientRow Nickname setter
     * @param nickname set as Client Nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
