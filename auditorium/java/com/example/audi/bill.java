package com.example.audi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class bill extends AppCompatActivity {

    TextView email_tv,timings;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        email_tv = (TextView) findViewById(R.id.email_tv);
        timings = (TextView) findViewById(R.id.timings_tv);
        email_tv.setText(firebaseUser.getEmail());
        avail a1 = new avail();
        String slots = a1.getSlots();
        timings.setText(slots);
        Toast.makeText(bill.this,"Here",Toast.LENGTH_SHORT).show();
    }
}
