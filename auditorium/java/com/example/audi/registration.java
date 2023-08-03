package com.example.audi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button regis_btn;
    private EditText regis_user,regis_email,regis_pass,regis_con_pass;
    private ImageView regis_dp;

    private FirebaseAuth firebaseAuth;
    int PICK_IMAGE_REQUEST = 111;
    FirebaseStorage storage ;
    StorageReference storageRef ;
    Uri filePath;

    private String username,email,password,confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initialise();

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        regis_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });




        regis_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = regis_user.getText().toString().trim();
                email = regis_email.getText().toString().trim();
                password = regis_pass.getText().toString().trim();
                confirmPassword = regis_con_pass.getText().toString().trim();

                if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || filePath==null){
                    if(filePath==null){
                        Toast.makeText(registration.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(registration.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(password.equals(confirmPassword)){
                        if(password.length()<6){
                            Toast.makeText(registration.this, "password should contain atleast 6 letters", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(registration.this, "user created", Toast.LENGTH_SHORT).show();
                                        uploadImg();
                                        sendEmailVerification();
                                    }
                                    else {
                                        Toast.makeText(registration.this, "Failed to add user", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });

                        }

                    }else{
                        Toast.makeText(registration.this, "Passwords not matched", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });


        Spinner spinner = findViewById(R.id.regis_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.posi, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initialise()
    {
        regis_btn = findViewById(R.id.regis_btn);
        regis_con_pass = findViewById(R.id.regis_con_pass);
        regis_user = findViewById(R.id.regis_user);
        regis_email = findViewById(R.id.regis_email);
        regis_pass = findViewById(R.id.regis_pass);
        regis_con_pass = findViewById(R.id.regis_con_pass);
        regis_dp = findViewById(R.id.regis_dp);
    }
    private void sendEmailVerification(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(registration.this, "An verification mail has been send to your email!", Toast.LENGTH_SHORT).show();
                    finish();
                    firebaseAuth.signOut();
                    startActivity(new Intent(registration.this,login.class));

                }
                else {
                    regis_con_pass.setText("");
                    regis_pass.setText("");
                    regis_user.setText("");
                    regis_email.setText("");
                    Toast.makeText(registration.this, "There was an error. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadImg(){
        if(filePath != null) {

            StorageReference childRef = storageRef.child(username);
            UploadTask uploadTask;
            uploadTask = childRef.putFile(filePath);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(registration.this, "Upload successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(registration.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(registration.this, "Select an image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                regis_dp.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
}
