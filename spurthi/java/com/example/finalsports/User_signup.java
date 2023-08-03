package com.example.finalsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

public class User_signup extends AppCompatActivity {

    String User_Name,User_Email,User_Password,User_RePassword,User_Phone,User_Age,User_Gender;
    EditText UName,UEmail,UPassword,URePassword,UPhone,UAge;
    RadioButton User_radgender;
    RadioGroup User_radselect;
    Button Create_New_User;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    //DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        //reff = FirebaseDatabase.getInstance().getReference("Users");
        Create_New_User = findViewById(R.id.Create_User);
        Create_New_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });


    }

    public void check(){
        UName = findViewById(R.id.User_Name);
        UEmail = findViewById(R.id.User_EmailId);
        UAge = findViewById(R.id.User_Age);
        UPhone = findViewById(R.id.User_Phone);
        UPassword = findViewById(R.id.User_password);
        URePassword = findViewById(R.id.User_Re_Password);
        User_radselect = findViewById(R.id.User_Gender);
        int radioID = User_radselect.getCheckedRadioButtonId();
        User_radgender = findViewById(radioID);

        User_Gender = User_radgender.getText().toString().trim();


        User_Name = UName.getText().toString().trim();
        User_Email = UEmail.getText().toString().trim();
        User_Age = UAge.getText().toString().trim();
        User_Phone = UPhone.getText().toString().trim();
        User_Password = UPassword.getText().toString().trim();
        User_RePassword = URePassword.getText().toString().trim();

        if(User_Name.isEmpty()) {
            UName.setError("Please Enter Name");
            UName.requestFocus();
            return;
        }

        if(User_Age.isEmpty()){
            UAge.setError("Please Enter Age");
            UAge.requestFocus();
            return;
        }

        if(User_Email.isEmpty()) {
            UEmail.setError("Please Enter e-mail");
            UEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(User_Email).matches()){
            UEmail.setError("Please Enter valid e-mail");
            UEmail.requestFocus();
            return;
        }

        if(User_Password.isEmpty()) {
            UPassword.setError("Please Enter  password");
            UPassword.requestFocus();
            return;
        }
        if(User_Password.length()<6)
        {
            UPassword.setError("");
            UPassword.requestFocus();
            return;
        }

        if(!User_Password.equals(User_RePassword)){
            URePassword.setError("Password should be at least 6 characters long");
            URePassword.requestFocus();
        }

        if(User_Phone.isEmpty()) {
            UPhone.setError("Please Enter Phone Number");
            UPhone.requestFocus();
            return;
        }
        if(User_Phone.length()!=10)
        {
            UPhone.setError("Number Should be 10 Digits");
            UPhone.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(User_Email,User_Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(User_signup.this,"New account created Successfully!",Toast.LENGTH_SHORT).show();
                    addData();
                }
                else{
                    Toast.makeText(User_signup.this,"User Already Exists",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void addData(){
        String id = User_Email.split("@")[0];
        user ur = new user(User_Name,User_Age, User_Email, User_Phone, User_Gender);
        myRef.child(College.getCollegeName()).child("Users").child(id).setValue(ur);
        //reff.child(id).setValue(ur);
        next();

    }

    public void next(){
        Intent intent = new Intent(User_signup.this,User_login.class);
        startActivity(intent);
    }
}
