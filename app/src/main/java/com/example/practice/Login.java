package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");


        sharedPreferences = getSharedPreferences("OutlayData",MODE_PRIVATE);
        editor =sharedPreferences.edit();

        binding.LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = binding.EmailAddressLogIn.getText().toString() ;
                String Password = binding.PasswordLogIn.getText().toString() ;



                if(TextUtils.isEmpty(Email)){
                    binding.EmailAddressLogIn.setError("Email cannot be empty");
                }
                if(TextUtils.isEmpty(Password)){
                    binding.PasswordLogIn.setError("Password cannot be empty");
                }
                if(Password.length() <6){
                    Toast.makeText(Login.this, "Password must contain 6 digits ", Toast.LENGTH_SHORT).show();
                }
                progressDialog.show();

                auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        FirebaseUser current_user = auth.getCurrentUser();
                        String Uid = current_user.getUid();
                        editor.putString("UID",Uid);
                        editor.apply();
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, " Login Succesfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(Login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });

        binding.forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Forgotpass.class));
                finish();
            }
        });




    }

    public void gotoregister(View view) {
        startActivity(new Intent(getApplicationContext(),Register_page.class));
        finish();
    }


}