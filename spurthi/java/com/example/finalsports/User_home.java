

package com.example.finalsports;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class User_home extends AppCompatActivity {
    Button view_Tournament;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        view_Tournament = findViewById(R.id.Tournament);
        view_Tournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go();
            }
        });
    }

    public void go(){
        Intent intent1 = new Intent(this,ViewTournaments.class);
        startActivity(intent1);
    }
    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this,Admin_login.class);
        startActivity(intent);
    }
}
/*
*list->spurthi
*list->sports
*add team or add player
* */