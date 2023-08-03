package com.example.audi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class forgotPassword extends AppCompatActivity {
    private TextView forgotEmailField;
    private Button forgotsendBtn;
    private FirebaseAuth firebaseAuth;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initializeViews();
        firebaseAuth = FirebaseAuth.getInstance();

        forgotsendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = forgotEmailField.getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(forgotPassword.this,"Please provide Email Address",Toast.LENGTH_LONG).show();
                }
                else {
                    sendForgotPasswordEmail();
                }
            }
        });

    }
    private void initializeViews(){
        forgotsendBtn = (Button) findViewById(R.id.forgotBtn);
        forgotEmailField = (TextView) findViewById(R.id.forgotEmail);
    }

    private void sendForgotPasswordEmail(){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(forgotPassword.this,"Email has been send!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(forgotPassword.this,login.class));
                    finish();
                }else {
                    Toast.makeText(forgotPassword.this,"There was an error! Please try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
