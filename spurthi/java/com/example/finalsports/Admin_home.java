package com.example.finalsports;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Admin_home extends AppCompatActivity {
    public static int operation=1;
    private Button btnNewAdmin,btnNewTournament,btnUpdateTournament;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        btnNewAdmin = findViewById(R.id.btnNewAdmin);
        btnNewAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_home.this,New_Admin.class);
                startActivity(intent);
            }
        });

        btnNewTournament = findViewById(R.id.btnNewTournament);
        btnNewTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation=Admin_Operations.CREATE_TOURNAMENT;
                Intent intent = new Intent(Admin_home.this,Tournament_Register.class);
                startActivity(intent);
            }
        });

        btnUpdateTournament=(Button) findViewById(R.id.btnUpdateTournament);
        btnUpdateTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation=Admin_Operations.UPDATE_TOURNAMENT;
                Intent intent=new Intent(Admin_home.this,ViewTournaments.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed(){
//        Intent i=new Intent(this,Admin_login.class);
//        startActivity(i);
    }
}
