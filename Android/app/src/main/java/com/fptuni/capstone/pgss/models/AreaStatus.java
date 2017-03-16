package com.fptuni.capstone.pgss.models;

/**
 * Created by TrungTNM on 3/15/2017.
 */

public enum AreaStatus {
    Deactive(0, "Deactive"),
    Active(1, "Active"),
    Unknown(4, "Unknown");

    private String name;
    private int id;

    private AreaStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static AreaStatus getById(int id) {
        for (AreaStatus status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        return Unknown;
    }
}
