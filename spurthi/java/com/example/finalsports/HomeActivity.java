package com.example.finalsports;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    static int mode;
    College college;
    private Button btnAdminLogin,btnUserLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        college=new College("Vesp");

        btnAdminLogin = (Button) findViewById(R.id.btnAdminLogin);
        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_Activity(1);
            }
        });

        btnUserLogin = (Button) findViewById(R.id.btnUserLogin);
        btnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_Activity(2);
            }
        });
    }

    public void open_Activity(int flag) {
        if (flag == 1) {
            mode= Login_Mode.ADMIN;
            Intent intent = new Intent(this, Admin_login.class);
            startActivity(intent);
        }
        if (flag == 2) {
            mode= Login_Mode.USER;
            Intent intent = new Intent(this, User_login.class);
            startActivity(intent);
        }
    }
}
