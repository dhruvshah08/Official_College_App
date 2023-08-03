package com.example.finalsports;

import android.widget.CheckedTextView;

public class Custom_Choice {
    private String message;
    private boolean isChecked;
    public Custom_Choice(String message,boolean isChecked){
        this.message=message;
        this.isChecked=isChecked;
    }
    public String getMessage() {
        return this.message;
    }
    public boolean isChecked(){
        return this.isChecked;
    }
    public void setMessage(String message){
        this.message=message;
    }
    public void setChecked(boolean isChecked){
        this.isChecked=isChecked;
    }

}
