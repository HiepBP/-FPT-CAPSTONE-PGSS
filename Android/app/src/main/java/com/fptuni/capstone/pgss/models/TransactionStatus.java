package com.fptuni.capstone.pgss.models;

/**
 * Created by TrungTNM on 3/12/2017.
 */

public enum TransactionStatus {
    Pending(0, "Đang xử lý"),
    Reserved(1, "Đã đặt chỗ"),
    Finished(2, "Hoàn thành"),
    Canceled(3, "Đã hủy"),
    Unknown(4, "Không rõ");

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
