package com.example.finalsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

public class User_login extends AppCompatActivity {

    Button login;
    EditText Uemail,Upass;
    public String Uuseremail,Upassword;
    public  String username;
    TextView NewUser;
    //DatabaseReference reff;
    private DatabaseReference reff;
    boolean found=false;
    private FirebaseAuth mAuth;

    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        mAuth = FirebaseAuth.getInstance();
        login =  findViewById(R.id.User_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
        //reff = FirebaseDatabase.getInstance().getReference();
        NewUser = findViewById(R.id.New_User);
        NewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag =1;
                next();
            }
        });


    }

    public void check(){
        Uemail =  findViewById(R.id.User_email);
        Upass =   findViewById(R.id.User_password);
        Uuseremail = Uemail.getText().toString().trim();
        Upassword = Upass.getText().toString().trim();

        if(Uuseremail.isEmpty()) {
            Uemail.setError("Please Enter e-mail");
            Uemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Uuseremail).matches()){
            Uemail.setError("Please Enter valid e-mail");
            Uemail.requestFocus();
            return;
        }

        if(Upassword.isEmpty()) {
            Upass.setError("Please Enter password");
            Upass.requestFocus();
            return;
        }

        //Check if this account exists
        reff= FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Users");

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().equals(Uuseremail.split("@")[0])){
                        found=true;
                        break;
                    }
                }
                checkAuth();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void checkAuth() {
        if (found) {
            mAuth.signInWithEmailAndPassword(Uuseremail, Upassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        flag = 2;
                        next();
                    } else {
                        Toast.makeText(User_login.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void next(){
        if(flag == 1){
            Intent intent = new Intent(User_login.this, User_signup.class);
            startActivity(intent);
        }
        if(flag == 2) {
            Intent intent1 = new Intent(User_login.this,ViewTournaments.class);
            Toast.makeText(User_login.this,"Logged in as User",Toast.LENGTH_SHORT).show();
            startActivity(intent1);
        }
    }

}
