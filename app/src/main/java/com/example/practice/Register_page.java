package com.example.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_page extends AppCompatActivity {

    EditText musername,memail,mpassword;
    Button sign_up;
    TextView sign_in;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseuser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        musername = findViewById(R.id.UserName_sign_up);
        memail = findViewById(R.id.EmailAddress_sign_up);
        mpassword = findViewById(R.id.Password_sign_up);
        sign_up = findViewById(R.id.Sign_up);
        sign_in = findViewById(R.id.Sign_In);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        firebaseAuth = FirebaseAuth.getInstance();
        databaseuser = FirebaseDatabase.getInstance().getReference("USERS");

        sharedPreferences = getSharedPreferences("OutlayData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //checking if already have an account

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = musername.getText().toString();
                String email = memail.getText().toString();
                String password = mpassword.getText().toString();

                if(!name.equalsIgnoreCase("")){
                    if(!email.equalsIgnoreCase("")){
                        if(!password.equalsIgnoreCase("")){
                            if(password.length()>=6) {
                                registeruser(name, email, password);
                            }else{
                                Toast.makeText(Register_page.this, "Password must contain 6 characters", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(Register_page.this, "Please enter password", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(Register_page.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Register_page.this, "Please enter name", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void gotologin(View view) {
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
    public void registeruser(String name , String email , String password){
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 progressDialog.dismiss();

                if(task.isSuccessful()){
                    //save user information to database

                    FirebaseUser currentuser = firebaseAuth.getCurrentUser();
                    String Uid = currentuser.getUid();
                    UserInfo userInfo = new UserInfo(name,email,"");
                    databaseuser.child(Uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                editor.putString("UID",Uid);
                                editor.commit();
                                Toast.makeText(Register_page.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        }
                    });


                }
                else{
                    Toast.makeText(Register_page.this, "Error registering user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}