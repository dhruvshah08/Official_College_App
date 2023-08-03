package com.example.finalsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class New_Admin extends AppCompatActivity {

    EditText txtUsername,txtPhoneNumber,txtEmail,txtPassword,txtConfirmPassword;
    Button save;
    //DatabaseReference reff;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    String adminName,adminPhoneNumber,adminEmail,adminPassword,adminConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__admin);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail =  findViewById(R.id.txtEmail);
        txtPhoneNumber =  findViewById(R.id.txtPhoneNumber);
        txtPassword =  findViewById(R.id.txtPassword);
        txtConfirmPassword =  findViewById(R.id.txtConfirmPassword);
        //reff = FirebaseDatabase.getInstance().getReference("Vesp/Admin");
        mAuth = FirebaseAuth.getInstance();
        save =  findViewById(R.id.btnSave);
        myRef = FirebaseDatabase.getInstance().getReference();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    public void validate(){
        adminName = txtUsername.getText().toString().trim();
        adminEmail = txtEmail.getText().toString().trim();
        adminPhoneNumber = txtPhoneNumber.getText().toString().trim();
        adminPassword = txtPassword.getText().toString().trim();
        adminConfirmPassword = txtConfirmPassword.getText().toString().trim();

        if(adminName.isEmpty()) {
            txtUsername.setError("Please Enter Name");
            txtUsername.requestFocus();
            return;
        }
        if(adminPhoneNumber.isEmpty()) {
            txtPhoneNumber.setError("Please Enter Phone Number");
            txtPhoneNumber.requestFocus();
            return;
        }
        if(adminPhoneNumber.length()!=10)
        {
            txtPhoneNumber.setError("Number Should be 10 Digits");
            txtPhoneNumber.requestFocus();
            return;
        }

        if(adminEmail.isEmpty()) {
            txtEmail.setError("Please Enter an e-mail");
            txtEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(adminEmail).matches()){
            txtEmail.setError("Please Enter valid e-mail");
            txtEmail.requestFocus();
            return;
        }

        if(adminPassword.isEmpty()) {
            txtPassword.setError("Please Enter a password");
            txtPassword.requestFocus();
            return;
        }
        if(adminPassword.length()<6)
        {
            txtPassword.setError("Password should be at least 6 characters long");
            txtPassword.requestFocus();
            return;
        }

        if(adminConfirmPassword.isEmpty()) {
            txtConfirmPassword.setError("Please Enter Confirm Password");
            txtConfirmPassword.requestFocus();
            return;
        }
        if(!adminPassword.equals(adminConfirmPassword))
        {
            txtConfirmPassword.setError("Password doesn't match");
            txtConfirmPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(adminEmail,adminPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()) {
               Toast.makeText(New_Admin.this,"New Admin Created Successfully!",Toast.LENGTH_SHORT).show();
               addData();
            }
            else{
                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(New_Admin.this,"Admin already exists",Toast.LENGTH_SHORT).show();
                }
            }
            }
        });

    }

    public void addData(){
        String id = adminEmail.split("@")[0];;
        admin ad = new admin(adminName,adminEmail,adminPhoneNumber);
        myRef.child(College.getCollegeName()).child("Admins").child(id).setValue(ad);
        //reff.child(id).setValue(ad);
        go();
    }

    public void go(){
        Intent intent = new Intent(this,Admin_home.class);
        startActivity(intent);
    }
}