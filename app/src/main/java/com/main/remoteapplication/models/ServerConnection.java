package com.main.remoteapplication.models;

public abstract class ServerConnection{

    public ServerConnection(String remoteIp, int portNumber) {
        this.remoteIp = remoteIp;
        this.portNumber = portNumber;
    }

    String remoteIp;
    int portNumber;

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }
}
