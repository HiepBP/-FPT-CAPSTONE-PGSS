package com.fptuni.capstone.pgss.models;

/**
 * Created by TrungTNM on 3/12/2017.
 */

public enum TransactionStatus {
    Pending(0, "Pending"),
    Reserved(1, "Reserved"),
    Finished(2, "Finished"),
    Canceled(3, "Canceled"),
    Unknown(4, "Unknown");

    private String name;
    private int id;

    private TransactionStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static TransactionStatus getById(int id) {
        for (TransactionStatus status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        return Unknown;
    }
}
