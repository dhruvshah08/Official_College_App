package com.example.audi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class about extends AppCompatActivity {
    private ViewPager viewp;
    private Slider slider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        viewp = (ViewPager)findViewById(R.id.viewpager);
        slider= new Slider(this);
        viewp.setAdapter(slider);

    }
}
