package com.example.practice;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Adapter_Earn  extends RecyclerView.Adapter<Adapter_Earn.Amount_holder> {



    String earn_id ="", earn_date="",earn_activity="";
    Update_interface update_interface;

    Context context;
    ArrayList<Earndatabase> Earn_list = new ArrayList<>();
    public Adapter_Earn(Context con , ArrayList<Earndatabase> list){
        context =con;
        Earn_list =list;
        update_interface = (Update_interface)context;
    }

    @NonNull
    @Override
    public Amount_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_recycler,parent,false);
        Amount_holder amount_holder = new Amount_holder(view);
        return amount_holder;


    }

    @Override
    public void onBindViewHolder(@NonNull Amount_holder holder, int position) {
        Earndatabase earndatabase = Earn_list.get(position);
        String amount = earndatabase.getEarnAmount();
        String date = earndatabase.getEarnDate();
        String activity = earndatabase.getEarnActivity();

        holder.Earn_row.setText("• "+amount);
        holder.Earn_activity.setText(activity);
        holder.Earn_date.setText(date);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Earndatabase earndatabase1 = Earn_list.get(position);
                show_Dialog(earndatabase1);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Earndatabase earndatabase1 = Earn_list.get(position);
                delete_amount(earndatabase1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Earn_list.size();
    }

    public class Amount_holder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_earn_row)
        TextView Earn_row;
        @BindView(R.id.text_date)
        TextView Earn_date;
        @BindView(R.id.Edit_Earn_amount)
        ImageView imageView;
        @BindView(R.id.Delete_button)
        ImageView delete;
        @BindView(R.id.text_activity)
        TextView Earn_activity;



        public Amount_holder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }

    public  void show_Dialog(final Earndatabase earn_Obj){



        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_earn_change);
        dialog.show();

        EditText editText = (EditText) dialog.findViewById(R.id.update_entry);
        Button button = (Button) dialog.findViewById(R.id.update_button_earn);
        earn_id=earn_Obj.getEarnId();


        editText.setText(earn_Obj.getEarnAmount());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM YYYY");
                Calendar calendar = Calendar.getInstance();
                earn_date = simpleDateFormat.format(calendar.getTime());

                String new_amount = editText.getText().toString();

                Earndatabase earndatabase = new Earndatabase(new_amount,earn_date,earn_id,"");
                update_interface.update_Earn_Database(earndatabase);
            }
        });
    }

    public void delete_amount(final Earndatabase earndatabase1){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.show();

        TextView textView = (TextView) dialog.findViewById(R.id.text_delete);
        Button yes_button = (Button) dialog.findViewById(R.id.yes_delete);
        Button no_button = (Button) dialog.findViewById(R.id.no_delete);
        int green = Color.parseColor("#3cba54");
        yes_button.setBackgroundColor(green);
        no_button.setBackgroundColor(Color.parseColor("#db3236"));


        textView.setText("Delete Amount (" + earndatabase1.getEarnAmount() + ") :");

        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                update_interface.delete_Earn_Database(earndatabase1);

            }
        });
    }
}

