package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Earn_activity extends AppCompatActivity {

    EditText entered_text ;
    Button add_entry;
    DatabaseReference databaseEarn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn_activity);


        entered_text = (EditText) findViewById(R.id.earn_entry);
        add_entry = (Button) findViewById(R.id.add_button_earn);
        spinner = (Spinner) findViewById(R.id.spinner_earn);

        sharedPreferences = getSharedPreferences("OutlayData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String userId = sharedPreferences.getString("UID","");

        databaseEarn = FirebaseDatabase.getInstance().getReference("Earn_Amount").child(userId);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        String todayDate = simpleDateFormat.format(calendar.getTime());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.spinner_earn));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        int selected_spinner_item = spinner.getSelectedItemPosition();
        String spinner_position = (String) spinner.getItemAtPosition(selected_spinner_item);




        add_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 String amount1 = entered_text.getText().toString();
                 String data = spinner.getSelectedItem().toString();

                if (!"".equals(amount1)){

                    if(spinner_position.isEmpty()){
                        Toast.makeText(Earn_activity.this, "Please select an Activity..", Toast.LENGTH_SHORT).show();
                    }

                    else {



                        String key = databaseEarn.push().getKey();
                        Earndatabase usernotes = new Earndatabase(amount1, todayDate, key, data);
                        databaseEarn.child(key).setValue(usernotes).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Earn_activity.this, "Amount added", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Earn_activity.this, "Error!! ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
                else
                    Toast.makeText(Earn_activity.this, "Enter a amount", Toast.LENGTH_SHORT).show();


            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bar);
//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(2);
        menuItem.setChecked(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                    case R.id.home:
                        Intent in_home = new Intent(Earn_activity.this,MainActivity.class);
                        startActivity(in_home);
                        break;
                    case R.id.earn:
                        Toast.makeText(Earn_activity.this, "This is Earn Page", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.spend:
                        Intent in_spend = new Intent(Earn_activity.this,Spend_Activity.class);
                        startActivity(in_spend);
                        break;

                    case R.id.total:
                        Intent in_total = new Intent(Earn_activity.this,TotalActivity.class);
                        startActivity(in_total);
                        break;

                }


                return false;
            }
        });
    }
}