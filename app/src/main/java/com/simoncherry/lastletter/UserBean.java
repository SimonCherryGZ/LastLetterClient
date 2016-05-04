package com.simoncherry.lastletter;

public class UserBean {
    private String id;
    private String user_name;
    private String user_password;
    private String user_letter;
    private int total_lost;
    private int countdown;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setUserName(String name) {
        this.user_name = name;
    }

    public String getUserName() {
        return this.user_name;
    }

    public void setUserPassword(String password) {
        this.user_password = password;
    }

    public String getUserPassword() {
        return this.user_password;
    }

    public void setUserLetter(String letter) {
        this.user_letter = letter;
    }

    public String getUserLetter() {
        return this.user_letter;
    }

    public void setTotalLost(int lost) {
        this.total_lost = lost;
    }

    public int getTotalLost() {
        return this.total_lost;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public int getCountdown() {
        return this.countdown;
    }
}