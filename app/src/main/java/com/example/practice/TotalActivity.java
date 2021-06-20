package com.example.practice;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class TotalActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseReference databaseReference;
    DatabaseReference firebaseDatabase;
    HashMap<String , String> Earn_display_activity = new HashMap<String, String>();
    HashMap<String , String> Spend_display_activity = new HashMap<String, String>();
    HashMap<String , Integer> color = new HashMap<String, Integer>();
    ProgressDialog progressDialog;
    DatabaseReference spend_reference;
    PieChartView pieChartView_earn;
    PieChartView pieChartView_spend;
    List<SliceValue> pie_data_earn = new ArrayList<>();
    List<SliceValue> pie_data_spend = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        pieChartView_earn = (PieChartView) findViewById(R.id.pie_chart);
        pieChartView_spend = (PieChartView) findViewById(R.id.pie_spend_chart);


        sharedPreferences = getSharedPreferences("OutlayData", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");

        String uId = sharedPreferences.getString("UID", "");
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Earn_Amount");
        databaseReference = firebaseDatabase.child(uId);
        
        spend_reference=FirebaseDatabase.getInstance().getReference("Spend_Amount").child(uId);

        color.put("Food",Color.parseColor("#A77FCD"));
        color.put("Fun",Color.parseColor("#C5C5CF"));
        color.put("Outing",Color.parseColor("#DCB171"));
        color.put("Groceries",Color.parseColor("#CDDC39"));
        color.put("Girlfriend",Color.parseColor("#DC2A27"));
        color.put("Rent",Color.parseColor("#66BB6A"));
        color.put("Shopping",Color.parseColor("#29B6F6"));
        color.put("Salary",Color.parseColor("#A77FCD"));
        color.put("Bonus",Color.parseColor("#C5C5CF"));
        color.put("Interest",Color.parseColor("#DCB171"));
        color.put("Profit",Color.parseColor("#CDDC39"));
        color.put("Relatives",Color.parseColor("#DC2A27"));
        color.put("Pocket Money",Color.parseColor("#66BB6A"));
        color.put("Others",Color.parseColor("#29B6F6"));



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bar);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home:
                        Intent in_home = new Intent(TotalActivity.this, MainActivity.class);
                        startActivity(in_home);
                        break;

                    case R.id.earn:
                        startActivity(new Intent(TotalActivity.this, Earn_activity.class));
                        break;

                    case R.id.spend:
                        Intent in_spend = new Intent(TotalActivity.this, Spend_Activity.class);
                        startActivity(in_spend);
                        break;

                    case R.id.total:
                        Toast.makeText(TotalActivity.this, "Already in this page...", Toast.LENGTH_SHORT).show();
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
        get_Earn_data();
        get_spend_data();

    }

  


    public void get_Earn_data() {


        ValueEventListener valueEventListener = new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Earndatabase earndatabase = ds.getValue(Earndatabase.class);
                    float t= 0;
                    String value = earndatabase.getEarnActivity();
                    String data = earndatabase.getEarnAmount();
                    boolean check = Earn_display_activity.containsKey(value);
                    if(check==true) {
                        t = Float.parseFloat(Earn_display_activity.get(value));
                    }
                    t =t + Float.parseFloat(data);
                    Earn_display_activity.put(value,String.valueOf(t));
                }

                List<String> list = new ArrayList<>(Earn_display_activity.keySet());

                float f = 0 ;
                for(int i = 0 ; i < list.size();i++){
                    f = f + Float.parseFloat(Earn_display_activity.get(list.get(i)));
                }

                for(int i = 0 ; i < list.size();i++){
                    float percent = 0;
                    percent = ((Float.parseFloat(Earn_display_activity.get(list.get(i))))/f)*100;
                    pie_data_earn.add(new SliceValue(Float.parseFloat(Earn_display_activity.get(list.get(i))), color.get(list.get(i))).setLabel(String.valueOf((int)percent)+"%"));

                }



                PieChartData pieChartData = new PieChartData(pie_data_earn);
                pieChartData.setHasLabels(true);
                pieChartData.setValueLabelsTextColor(R.color.grayBackground);
                pieChartData.setHasCenterCircle(true).setCenterText1("Earn Percentage").setCenterText1FontSize(12).setCenterText1Color(Color.parseColor("#e5ffcc")).setCenterText1Typeface(Typeface.DEFAULT_BOLD);

                pieChartView_earn.setPieChartData(pieChartData);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }

    private void get_spend_data() {
        
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    SpendDatabase spendDatabase = ds.getValue(SpendDatabase.class);
                    float t= 0;
                    String value = spendDatabase.getSpendActivity();
                    String data = spendDatabase.getSpendAmount();
                    boolean check = Spend_display_activity.containsKey(value);
                    if(check==true) {
                        t = Float.parseFloat(Spend_display_activity.get(value));
                    }
                    t =t + Float.parseFloat(data);
                    Spend_display_activity.put(value,String.valueOf(t));
                }

                List<String> list = new ArrayList<>(Spend_display_activity.keySet());

                float f = 0 ;
                for(int i = 0 ; i < list.size();i++){
                    f = f + Float.parseFloat(Spend_display_activity.get(list.get(i)));
                }

                for(int i = 0 ; i < list.size();i++){
                    float percent = 0;
                    percent = ((Float.parseFloat(Spend_display_activity.get(list.get(i))))/f)*100;
                    pie_data_spend.add(new SliceValue(Float.parseFloat(Spend_display_activity.get(list.get(i))), color.get(list.get(i))).setLabel(String.valueOf((int)percent)+"%"));

                }



                PieChartData pieChartData = new PieChartData(pie_data_spend);
                pieChartData.setHasLabels(true);
                pieChartData.setValueLabelsTextColor(R.color.grayBackground);
                pieChartData.setHasCenterCircle(true).setCenterText1("Spend Percentage").setCenterText1FontSize(12).setCenterText1Color(Color.parseColor("#e5ffcc")).setCenterText1Typeface(Typeface.DEFAULT_BOLD);

                pieChartView_spend.setPieChartData(pieChartData);



                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        spend_reference.addListenerForSingleValueEvent(valueEventListener);
        
        
    }
}
