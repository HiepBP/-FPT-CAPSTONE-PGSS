package com.fptuni.capstone.pgss.models;

/**
 * Created by TrungTNM on 3/12/2017.
 */

public enum TransactionStatus {
    Reserved(0, "Reserved"),
    Finished(1, "Finished"),
    Canceled(2, "Canceled"),
    Unknown(3, "Unknown");

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
