package com.example.practice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.fonts.Font;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import butterknife.internal.Constants;

public class Splashactivity extends AppCompatActivity {
    boolean online;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);
        getSupportActionBar().hide();


        online = isOnline();
        if (online == true) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splashactivity.this, Register_page.class));
                    finish();
                }
            }, 1000);
        }
            else{
                try {
                    AlertDialog.Builder builder =new AlertDialog.Builder(this);
                    builder.setTitle("No internet Connection");
                    builder.setMessage("Please turn on internet connection to continue");
                    builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } catch (Exception e) {
                    Log.d("Network",e.getMessage());

                }
            }
        }


        public boolean isOnline () {
            ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
                Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }





}