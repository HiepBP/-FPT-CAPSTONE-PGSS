package com.fptuni.capstone.pgss.network;

/**
 * Created by TrungTNM on 2/24/2017.
 */

public class ControlPubnubPackage {
    private String hub_name;
    private String device_name;
    private String command;

    public ControlPubnubPackage() {
    }

    public String getHub_name() {
        return hub_name;
    }

    public void setHub_name(String hub_name) {
        this.hub_name = hub_name;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
