    package com.example.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

    public class MainActivity extends AppCompatActivity {

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        DatabaseReference databaseReference;
        TextView Earn_Amount;
        TextView Spend_Amount;
        TextView Save_Amount;
        DatabaseReference firebaseDatabase;
        DatabaseReference Spend_Ref;
        ProgressDialog progressDialog;
        static float earn_g=0,spend_g=0,save=0;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            Earn_Amount = (TextView) findViewById(R.id.Earn_Amount);
            Spend_Amount = (TextView) findViewById(R.id.Spend_Amount);
            Save_Amount = findViewById(R.id.save_Amount);
            sharedPreferences = getSharedPreferences("OutlayData", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading Data...");

            String uId = sharedPreferences.getString("UID", "");
            firebaseDatabase = FirebaseDatabase.getInstance().getReference("Earn_Amount");
            databaseReference = firebaseDatabase.child(uId);

            Spend_Ref = FirebaseDatabase.getInstance().getReference("Spend_Amount").child(uId);



            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bar);
//          BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
            Menu menu = bottomNavigationView.getMenu();
            MenuItem menuItem = menu.getItem(0);
            menuItem.setChecked(true);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.home:
                            Toast.makeText(MainActivity.this, "This is home page", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.spend:
                            Intent in = new Intent(MainActivity.this, Spend_Activity.class);
                            startActivity(in);
                            break;

                        case R.id.earn:
                            Intent in_spend = new Intent(MainActivity.this, Earn_activity.class);
                            startActivity(in_spend);
                            break;

                        case R.id.total:
                            Intent in_total = new Intent(MainActivity.this,TotalActivity.class);
                            startActivity(in_total);
                            break;



                    }


                    return false;
                }
            });
        }

        @Override
        protected void onResume() {
            super.onResume();
            progressDialog.show();
            get_Earn_Amount();
            ger_Spend_amount();




        }



        public void LogOut(View view) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }


        public void gotoEarn_List(View view) {

            startActivity(new Intent(getApplicationContext(), View_Earn_Data.class));

        }

        public void gotoSpend_List(View view) {
            startActivity(new Intent(getApplicationContext(), View_Spend_Data.class));

        }


        public void get_Earn_Amount() {


            ValueEventListener valueEventListener = new ValueEventListener() {
                float earn=0;

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Earndatabase earndatabase = ds.getValue(Earndatabase.class);
                        String value = earndatabase.getEarnAmount();
                        if(!value.isEmpty()) {
                            earn = earn + Float.parseFloat(value);
                        }
                    }
                    earn_g = earn;
                    Earn_Amount.setText(String.valueOf(earn));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            databaseReference.addListenerForSingleValueEvent(valueEventListener);

        }

        public void ger_Spend_amount(){
            ValueEventListener valueEventListener = new ValueEventListener() {
                float spend=0;
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren()){
                        SpendDatabase spendDatabase = ds.getValue(SpendDatabase.class);
                        String value = spendDatabase.getSpendAmount();
                        if(!value.isEmpty()) {
                            spend = spend + Float.parseFloat(value);
                        }
                    }
                    spend_g=spend;
                    Spend_Amount.setText(String.valueOf(spend));
                    save = earn_g - spend_g;
                    Save_Amount.setText(String.valueOf(save));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            Spend_Ref.addListenerForSingleValueEvent(valueEventListener);
            progressDialog.dismiss();
        }
    }