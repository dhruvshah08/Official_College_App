package com.example.audi;

public class checkAvail {

    public int dd,mm,yy;
    boolean slot1,slot2,slot3;

    public checkAvail(int dd, int mm, int yy, boolean slot1, boolean slot2, boolean slot3) {
        this.dd = dd;
        this.mm = mm;
        this.yy = yy;
        this.slot1 = slot1;
        this.slot2 = slot2;
        this.slot3 = slot3;
    }

    public checkAvail() {

    }

    public int getDd() {
        return dd;
    }

    public void setDd(int dd) {
        this.dd = dd;
    }

    public int getMm() {
        return mm;
    }

    public void setMm(int mm) {
        this.mm = mm;
    }

    public int getYy() {
        return yy;
    }

    public void setYy(int yy) {
        this.yy = yy;
    }

    public boolean isSlot1() {
        return slot1;
    }

    public void setSlot1(boolean slot1) {
        this.slot1 = slot1;
    }

    public boolean isSlot2() {
        return slot2;
    }

    public void setSlot2(boolean slot2) {
        this.slot2 = slot2;
    }

    public boolean isSlot3() {
        return slot3;
    }

    public void setSlot3(boolean slot3) {
        this.slot3 = slot3;
    }
}
