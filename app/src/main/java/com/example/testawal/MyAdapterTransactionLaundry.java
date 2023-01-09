package com.example.testawal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class MyAdapterTransactionLaundry extends RecyclerView.Adapter<MyAdapterTransactionLaundry.MyViewHolder>{

    Context context;
    ArrayList<Transaction> transactionList;
    Dialog myDialog;

    public MyAdapterTransactionLaundry(Context context, ArrayList<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public MyAdapterTransactionLaundry.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout inflater = membuat view yang sudah dibuat yaitu laundrylist_home berulang-ulang
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transactionlist_laundry, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        //saat dipanggil berulang-ulang akan return ke new ViewHolder
        //akan memanggil MyViewHolder

        //toast list transaksi laundry
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "nama = " + transactionList.get(viewHolder.getAdapterPosition()).getNamaUser(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, OrderDetailLaundryNew.class);
                intent.putExtra("orderid", transactionList.get(viewHolder.getAdapterPosition()).getId());
                intent.putExtra("namaLaundry1", transactionList.get(viewHolder.getAdapterPosition()).getNamaLaundry());
                intent.putExtra("namaUser1", transactionList.get(viewHolder.getAdapterPosition()).getNamaUser());
                context.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterTransactionLaundry.MyViewHolder holder, int position) {
        //isi dulu attribute myViewHolder
        //saat tarik viewHolder, kita isi di posisi berapa
        holder.myText1.setText(transactionList.get(position).getNamaUser());
        holder.myText2.setText(transactionList.get(position).getHarga());
        if(transactionList.get(position).getStatus().toString().equals("0")){
            holder.myText3.setText("Awaiting");
        }
        else if(transactionList.get(position).getStatus().toString().equals("1")){
            holder.myText3.setText("Accepted");
        }
        else if(transactionList.get(position).getStatus().toString().equals("2")){
            holder.myText3.setText("On Pickup");
        }
        else if(transactionList.get(position).getStatus().toString().equals("3")){
            holder.myText3.setText("On Process");
        }
        else if(transactionList.get(position).getStatus().toString().equals("4")){
            holder.myText3.setText("Done");
        }
        else if(transactionList.get(position).getStatus().toString().equals("5")){
            holder.myText3.setText("Cancelled");
        }
        holder.myText4.setText(transactionList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //    isi dari viewHolder yaitu namaLaundry dan harga
        TextView myText1, myText2, myText3, myText4;
        CardView parentLayout;
        //    membuat new viewHolder
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //    itemView. artinya isi per item atau per row
            myText1 = itemView.findViewById(R.id.txtNamaUser);
            myText2 = itemView.findViewById(R.id.txtHarga);
            myText3 = itemView.findViewById(R.id.txtStatus);
            myText4 = itemView.findViewById(R.id.txtId);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }


}