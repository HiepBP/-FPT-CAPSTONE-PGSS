package com.fptuni.capstone.pgss.models;

/**
 * Created by TrungTNM on 3/15/2017.
 */

public enum ParkingLotStatus {
    Deactive(0, "Deactive"),
    Active(1, "Active"),
    Reserved(2, "Reserved"),
    Nonavailable(3, "Non-available"),
    Unknown(4, "Unknown");

    private String name;
    private int id;

    private ParkingLotStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ParkingLotStatus getById(int id) {
        for (ParkingLotStatus status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        return Unknown;
    }
}
