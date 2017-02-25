package com.fptuni.capstone.pgss.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungTNM on 2/24/2017.
 */

public class ControlPubnubPackage {
    @SerializedName("hub_name")
    private String hubName;
    @SerializedName("device_name")
    private String deviceName;
    @SerializedName("command")
    private String command;

    public ControlPubnubPackage() {
    }

    public String getHubName() {
        return hubName;
    }

    public void setHubName(String hubName) {
        this.hubName = hubName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
