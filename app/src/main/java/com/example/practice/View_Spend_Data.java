package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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

public class View_Spend_Data extends AppCompatActivity implements Update_spend_interface {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    ArrayList<SpendDatabase> SpendData = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__spend__data);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Reading amounts...");

        sharedPreferences = getSharedPreferences("OutlayData",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String uId = sharedPreferences.getString("UID","");
        databaseReference = FirebaseDatabase.getInstance().getReference("Spend_Amount").child(uId);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_Spend_data);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ReadAmount();
    }

    public void ReadAmount(){
        SpendData.clear();
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    SpendDatabase spend = snapshot1.getValue(SpendDatabase.class);
                    SpendData.add(spend);
                }
                progressDialog.dismiss();

                Adapter_Spend adapter_spend = new Adapter_Spend(View_Spend_Data.this,SpendData);
                recyclerView.setAdapter(adapter_spend);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void Update_Spend_database(SpendDatabase spendDatabase) {
        databaseReference.child(spendDatabase.getSpendId()).setValue(spendDatabase).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    ReadAmount();
                }
                else{
                    Toast.makeText(View_Spend_Data.this, "Error!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void Delete_Spend_database(SpendDatabase spendDatabase) {
        databaseReference.child(spendDatabase.getSpendId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    ReadAmount();
                }
                else{
                    Toast.makeText(View_Spend_Data.this, "Error!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}