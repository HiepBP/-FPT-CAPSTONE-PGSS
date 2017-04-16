package com.fptuni.capstone.pgss.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungTNM on 3/12/2017.
 */

public class Area {
    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("EmptyAmount")
    private int emptyAmount;
    @SerializedName("UpdateAvailable")
    private boolean updateAvailable;
    @SerializedName("Status")
    private int status;

    public Area() {
    }

    public Area(Area area) {
        id = area.getId();
        name = area.getName();
        emptyAmount = area.getEmptyAmount();
        updateAvailable = area.isUpdateAvailable();
        status = area.getStatus();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEmptyAmount() {
        return emptyAmount;
    }

    public void setEmptyAmount(int emptyAmount) {
        this.emptyAmount = emptyAmount;
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public void setUpdateAvailable(boolean updateAvailable) {
        this.updateAvailable = updateAvailable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
