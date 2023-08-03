package com.example.finalsports;

public class admin {
    public String username,email,password,phone;
    public admin(){

    }
    public admin(String username, String email,String phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
    }
    public String getUname() {
        return username;
    }

    public String getEmail() {
        return email;
    }


    public String getPhone() {
        return phone;
    }
}
