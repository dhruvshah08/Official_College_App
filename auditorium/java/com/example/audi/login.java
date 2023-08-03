package com.example.audi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//import static com.example.audi.R.drawable.error;

public class login extends AppCompatActivity {
    private EditText email,password;
    private Button loginBtn;
    private TextView forgotPassword,register;
    private String emailStr,passwordStr;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        forgotPassword = findViewById(R.id.login_forgot_password);
        register = findViewById(R.id.login_signup_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(login.this,auditorium.class));
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailStr = email.getText().toString().trim();
                passwordStr = password.getText().toString().trim();
                if(emailStr.isEmpty() || passwordStr.isEmpty()){
                    Toast.makeText(login.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    login();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,registration.class));

            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,forgotPassword.class));
            }
        });
    }
    private void login(){
        firebaseAuth.signInWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    checkEmailVerification();

                }
                else {
                    Toast.makeText(login.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void checkEmailVerification(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        boolean userVerified = user.isEmailVerified();

        if(userVerified){
            startActivity(new Intent(login.this,auditorium.class));
        }
        else {
            Toast.makeText(this, "Please verify your mail", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}