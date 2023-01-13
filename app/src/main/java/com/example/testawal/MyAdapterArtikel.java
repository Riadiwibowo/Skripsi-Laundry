package com.example.testawal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
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

public class MyAdapterArtikel extends RecyclerView.Adapter<MyAdapterArtikel.MyViewHolder> {

    String judul[];
    int images[];
    Context context;

    public MyAdapterArtikel(Context ct, String s1[], int img[]) {
        this.context = ct;
        judul = s1;
        images = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listartikel, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myText1.setText(judul[position]);
        holder.myImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return judul.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        SharedPreferences sharedPreferences;

        TextView myText1;
        ImageView myImage;

        CardView artikelLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            myText1 = itemView.findViewById(R.id.txtJudulArtikel);
            myImage = itemView.findViewById(R.id.imgArtikel);
            artikelLayout = itemView.findViewById(R.id.artikelLayout);

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            final int lightbluex = ContextCompat.getColor(context, R.color.lightbluex);
            final int greyimage = ContextCompat.getColor(context, R.color.greyimage);

            if (sharedPreferences.getBoolean("dark_mode", true)) {
                artikelLayout.setCardBackgroundColor(lightbluex);
                myImage.setBackgroundColor(greyimage);
            }
        }
    }
}
