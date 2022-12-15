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

public class MyAdapterTransaction extends RecyclerView.Adapter<MyAdapterTransaction.MyViewHolder>{

    Context context;
    ArrayList<Transaction> transactionList;
    Dialog myDialog;

    public MyAdapterTransaction(Context context, ArrayList<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public MyAdapterTransaction.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout inflater = membuat view yang sudah dibuat yaitu laundrylist_home berulang-ulang
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transactionlist_user, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        //saat dipanggil berulang-ulang akan return ke new ViewHolder
        //akan memanggil MyViewHolder

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterTransaction.MyViewHolder holder, int position) {
        //isi dulu attribute myViewHolder
        //saat tarik viewHolder, kita isi di posisi berapa
        holder.myText1.setText(transactionList.get(position).getNamaLaundry());
        holder.myText2.setText(transactionList.get(position).getHarga());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //    isi dari viewHolder yaitu namaLaundry dan harga
        TextView myText1, myText2;
        CardView parentLayout;
        //    membuat new viewHolder
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //    itemView. artinya isi per item atau per row
            myText1 = itemView.findViewById(R.id.txtNamaLaundry);
            myText2 = itemView.findViewById(R.id.txtHarga);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }


}
