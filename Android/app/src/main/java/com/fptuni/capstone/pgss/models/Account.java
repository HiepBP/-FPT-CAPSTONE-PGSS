package com.fptuni.capstone.pgss.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungTNM on 2/22/2017.
 */

public class Account {

    public static final String ROLE_USER = "ActiveUser";
    public static final String ROLE_MANAGER = "Manager";

    @SerializedName("Email")
    private String email;
    @SerializedName("Username")
    private String username;
    @SerializedName("FullName")
    private String fullname;
    @SerializedName("Password")
    private String password;
    @SerializedName("ConfirmPassword")
    private String confirmPassword;
    private String role;

    public Account() {
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
