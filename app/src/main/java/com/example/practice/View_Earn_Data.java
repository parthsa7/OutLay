package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;

public class    View_Earn_Data extends AppCompatActivity implements Update_interface{

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseReference databaseTotal;
    ProgressDialog progressDialog;
    ArrayList<Earndatabase> EarnData = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__earn__data);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Reading amounts...");

        sharedPreferences = getSharedPreferences("OutlayData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String uid = sharedPreferences.getString("UID","");
        databaseTotal = FirebaseDatabase.getInstance().getReference("Earn_Amount").child(uid);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_earn_data);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ReadEarnAmount();

    }

    public  void ReadEarnAmount(){
        EarnData.clear();
        progressDialog.show();

        databaseTotal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Earndatabase Earn = snapshot1.getValue(Earndatabase.class);
                    EarnData.add(Earn);
                }
                progressDialog.dismiss();

                Adapter_Earn adapter_earn = new Adapter_Earn(View_Earn_Data.this,EarnData);
                recyclerView.setAdapter(adapter_earn);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void update_Earn_Database(Earndatabase earndatabase) {
        databaseTotal.child(earndatabase.getEarnId()).setValue(earndatabase).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {

                    ReadEarnAmount();
                }
                else{
                    Toast.makeText(View_Earn_Data.this, "Error !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void delete_Earn_Database(Earndatabase earndatabase) {
        databaseTotal.child(earndatabase.getEarnId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    ReadEarnAmount();
                }
                else{
                    Toast.makeText(View_Earn_Data.this, "Error!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}