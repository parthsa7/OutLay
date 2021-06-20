package com.example.practice;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
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

public class Adapter_Spend extends RecyclerView.Adapter<Adapter_Spend.Amount_holder>  {

    Context context;
    ArrayList<SpendDatabase> Spend_Database = new ArrayList<>();
    String spend_id="",spend_date="";
    Update_spend_interface update_spend_interface;

    public Adapter_Spend(Context con , ArrayList<SpendDatabase> Spend){
        context = con;
        Spend_Database=Spend;
        update_spend_interface = (Update_spend_interface)context;
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

            SpendDatabase spendDatabase = Spend_Database.get(position);
            String amount = spendDatabase.getSpendAmount();
            String date = spendDatabase.getSpendDate();
            String activity = spendDatabase.getSpendActivity();

        holder.Earn_row.setText("• "+amount);
        holder.Earn_Activity.setText(activity);
        holder.Earn_date.setText(date);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SpendDatabase spendDatabase1=  Spend_Database.get(position);
               show_Dialog(spendDatabase1);

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpendDatabase spendDatabase1 = Spend_Database.get(position);
                delete_Amount(spendDatabase1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Spend_Database.size();
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
        TextView Earn_Activity;

        public Amount_holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void show_Dialog(SpendDatabase spendObj){

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_earn_change);
        dialog.show();

        EditText editText = (EditText) dialog.findViewById(R.id.update_entry);
        Button button = (Button) dialog.findViewById(R.id.update_button_earn);

        spend_id = spendObj.getSpendId();

        editText.setText(spendObj.getSpendAmount());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM YYYY");
                Calendar calendar = Calendar.getInstance();
                spend_date = simpleDateFormat.format(calendar.getTime());

                String new_amount = editText.getText().toString();

                SpendDatabase spendDatabase = new SpendDatabase(new_amount,spend_date,spend_id,"");
                update_spend_interface.Update_Spend_database(spendDatabase);
            }
        });
    }

    public void delete_Amount(SpendDatabase spendDatabase1){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.show();

        TextView textView = (TextView) dialog.findViewById(R.id.text_delete);
        Button yes_button = (Button) dialog.findViewById(R.id.yes_delete);
        Button no_button = (Button) dialog.findViewById(R.id.no_delete);
        int green = Color.parseColor("#3cba54");
        yes_button.setBackgroundColor(green);
        no_button.setBackgroundColor(Color.parseColor("#db3236"));

        textView.setText("Delete Amount (" +spendDatabase1.getSpendAmount()+") :");

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
                update_spend_interface.Delete_Spend_database(spendDatabase1);
            }
        });
    }


}
