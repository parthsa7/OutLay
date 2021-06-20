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
import android.widget.AdapterView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Spend_Activity extends AppCompatActivity {



    EditText entered_text ;
    Button add_entry;
    DatabaseReference databaseSpend;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spend_);


        entered_text = (EditText) findViewById(R.id.spend_entry);
        add_entry = (Button) findViewById(R.id.add_button);
        spinner =(Spinner) findViewById(R.id.spinner);


        sharedPreferences = getSharedPreferences("OutlayData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String userId = sharedPreferences.getString("UID","");


        databaseSpend = FirebaseDatabase.getInstance().getReference("Spend_Amount").child(userId);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        String todaydate = simpleDateFormat.format(calendar.getTime());

        //Spinner class
        ArrayAdapter<String> spinneradapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.spinner_item));
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinneradapter);




        add_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount1 = entered_text.getText().toString();
                String data = spinner.getSelectedItem().toString();

                if (!"".equals(amount1)) {
                    //spinner
                    if(data==null) {

                        Toast.makeText(Spend_Activity.this, "Please select a activity...", Toast.LENGTH_SHORT).show();

                    }
                    else {


                        String key = databaseSpend.push().getKey();
                        SpendDatabase usernotes = new SpendDatabase(amount1, todaydate, key , data);
                        databaseSpend.child(key).setValue(usernotes).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Spend_Activity.this, "Amount added", Toast.LENGTH_SHORT).show();
                             }
                                else {
                                    Toast.makeText(Spend_Activity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    }
                }

                else{
                    Toast.makeText(Spend_Activity.this, "Please Enter a Amount.", Toast.LENGTH_SHORT).show();
                }
            }
        });





        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bar);
//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        Intent in_home = new Intent(Spend_Activity.this,MainActivity.class);
                        startActivity(in_home);
                        break;
                    case R.id.earn:
                        Intent in = new Intent(Spend_Activity.this,Earn_activity.class);
                        startActivity(in);
                        break;

                    case R.id.spend:
                        Toast.makeText(Spend_Activity.this, "This is spend page", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.total:
                        Intent in_total = new Intent(Spend_Activity.this,TotalActivity.class);
                        startActivity(in_total);
                        break;


                }


                return false;
            }
        });

    }
}