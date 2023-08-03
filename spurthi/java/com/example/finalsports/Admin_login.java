package com.example.finalsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Admin_login extends AppCompatActivity {

    private Button btnLogin;
    private EditText txtEmail,txtPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference reff;
    boolean found=false;
    String Cuseremail,Cpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        mAuth = FirebaseAuth.getInstance();
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    public void check(){
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword =  (EditText) findViewById(R.id.txtPassword);
        Cuseremail = txtEmail.getText().toString().trim();
        Cpassword = txtPassword.getText().toString().trim();
        if("".equals(Cuseremail)){
            txtPassword.setError("Please enter Email");
            return;
        }
        if("".equals(Cpassword)){
            txtPassword.setError("Please enter password");
            return;
        }

        //Check if this account exists
        reff= FirebaseDatabase.getInstance().getReference(College.getCollegeName()+"/Admins");

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().equals(txtEmail.getText().toString().trim().split("@")[0])) {
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

    public void next(){
        Intent intent = new Intent(Admin_login.this,Admin_home.class);
        startActivity(intent);
    }
    private void checkAuth(){
        if(found){
            mAuth.signInWithEmailAndPassword(Cuseremail,Cpassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Admin_login.this,"Logged in as Admin",Toast.LENGTH_SHORT).show();
                        next();
                    }
                    else{
                        Toast.makeText(Admin_login.this,"Login unsuccessful!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(Admin_login.this,"Incorrect Email or Password",Toast.LENGTH_SHORT).show();
        }
    }



}
