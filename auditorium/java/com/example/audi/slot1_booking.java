package com.example.audi;

public class slot1_booking {

    String status;
    String user;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public slot1_booking() {
    }

    public slot1_booking(String status, String user) {
        this.status = status;
        this.user = user;
    }
}
