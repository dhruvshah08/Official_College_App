package com.example.finalsports;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int year;
    public Player() {
    }
    public Player(String name, int year) {
        this.name = name;
        this.year = year;
    }
    public String getName() {
        return this.name;
    }
    public int getYear() {
        return this.year;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setYear(int year){
        this.year=year;
    }
}
