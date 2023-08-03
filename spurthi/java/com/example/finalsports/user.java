package com.example.finalsports;

public class user {

    String age, email, phone, gender, name;

    user(){

    }

    public user(String name, String age, String email, String phone, String gender) {
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

}
