package com.example.audi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button,button1,button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.cv_about);
        button1 = (Button)findViewById(R.id.cv_audi);
        button2=(Button)findViewById(R.id.main_login);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAudi();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAbout();
            }
        });
    }

    public void startAbout(){
        Intent intent =  new Intent(this,about.class);
        startActivity(intent);
    }

    public void startAudi(){
        Intent intent =  new Intent(this,auditorium.class);
        startActivity(intent);
    }

    public void login(){
        Intent intent =  new Intent(this,login.class);
        startActivity(intent);
    }
}
