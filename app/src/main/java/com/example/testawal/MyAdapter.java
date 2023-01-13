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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    SharedPreferences sharedPreferences;

    ConstraintLayout constraintTop, parentLayout;

    Context context;
    ArrayList<User> userList = new ArrayList<>();
    Dialog myDialog;
    TextView Alamat, Description, Phone, Services, NamaLaundry;
    ImageView iconPopup;
    Button btnPopup;
    TextView Kg, Satuan, Pair, Pickup;

    public MyAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout inflater = membuat view yang sudah dibuat yaitu laundrylist_home berulang-ulang
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.laundrylist_home, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        //saat dipanggil berulang-ulang akan return ke new ViewHolder
        //akan memanggil MyViewHolder

        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.popup);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconPopup = myDialog.findViewById(R.id.iconPopup);
                Alamat = myDialog.findViewById(R.id.Alamat);
                Description = myDialog.findViewById(R.id.description);
                Phone = myDialog.findViewById(R.id.Telp);
                btnPopup = myDialog.findViewById(R.id.btnPopup);
                Services = myDialog.findViewById(R.id.Service);
                NamaLaundry = myDialog.findViewById(R.id.txtNamaLaundry);
                constraintTop = myDialog.findViewById(R.id.constraintTop);
                parentLayout = myDialog.findViewById(R.id.parentLayout);

                final Drawable popupatasdark = ContextCompat.getDrawable(context, R.drawable.popupatasdark);
                final Drawable backgroundmainpopupdark = ContextCompat.getDrawable(context, R.drawable.backgroundmainpopupdark);
                final int lightbluex = ContextCompat.getColor(context, R.color.lightbluex);
                final int white = ContextCompat.getColor(context, R.color.white);

                if (sharedPreferences.getBoolean("dark_mode", true)) {
                    constraintTop.setBackground(popupatasdark);
                    parentLayout.setBackground(backgroundmainpopupdark);
                    btnPopup.setBackgroundColor(lightbluex);
                    btnPopup.setTextColor(white);
                }

                if(userList.get(viewHolder.getAdapterPosition()).getImageUrl().toString().equals("")){
                    iconPopup.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_profile_icon));
                }else{
                    Glide.with(context).load(userList.get(viewHolder.getAdapterPosition()).getImageUrl()).into(iconPopup);
                }
                NamaLaundry.setText(userList.get(viewHolder.getAdapterPosition()).getNama());
                Phone.setText(userList.get(viewHolder.getAdapterPosition()).getPhone());

                Alamat.setText(userList.get(viewHolder.getAdapterPosition()).getAddress());

                Description.setText(userList.get(viewHolder.getAdapterPosition()).getDescription());

                if (userList.get(viewHolder.getAdapterPosition()).getServices().equals(null)){
                    Services.setText("-");
                }else{
                    Services.setText(userList.get(viewHolder.getAdapterPosition()).getServices());
                }
//                if (userList.get(viewHolder.getAdapterPosition()).getKiloan().equals(null)){
//                    Kg.setText("-");
//                }else{
//                    Kg.setText(userList.get(viewHolder.getAdapterPosition()).getKiloan());
//                }
//                Kg.setText(userList.get(viewHolder.getAdapterPosition()).getKiloan());
//                if (userList.get(viewHolder.getAdapterPosition()).getSatuan().equals(null)){
//                    Satuan.setText("-");
//                }else{
//                    Satuan.setText(userList.get(viewHolder.getAdapterPosition()).getSatuan());
//                }
//                Satuan.setText(userList.get(viewHolder.getAdapterPosition()).getSatuan());
//                if (userList.get(viewHolder.getAdapterPosition()).getSepatu().equals(null)){
//                    Pair.setText("-");
//                }else{
//                    Pair.setText(userList.get(viewHolder.getAdapterPosition()).getSepatu());
//                }
//                Pair.setText(userList.get(viewHolder.getAdapterPosition()).getSepatu());
//                if (userList.get(viewHolder.getAdapterPosition()).getPickup().equals(null)){
//                    Pickup.setText("-");
//                }else{
//                    Pickup.setText(userList.get(viewHolder.getAdapterPosition()).getPickup());
//                }
//                Pickup.setText(userList.get(viewHolder.getAdapterPosition()).getPickup());

                myDialog.show();
                btnPopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, OrderProcess.class);
                        intent.putExtra("namaLaundry", userList.get(viewHolder.getAdapterPosition()).getNama());
                        intent.putExtra("alamatLaundry", userList.get(viewHolder.getAdapterPosition()).getAddress());
                        intent.putExtra("noTelpLaundry", userList.get(viewHolder.getAdapterPosition()).getPhone());
                        context.startActivity(intent);
                    }
                });
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        //isi dulu attribute myViewHolder
        //saat tarik viewHolder, kita isi di posisi berapa

        Glide.with(context).load(userList.get(position).getImageUrl()).into(holder.myImage);
        holder.myText1.setText(userList.get(position).getNama());
        holder.myText2.setText(userList.get(position).getDescription());
//        holder.myImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //    isi dari viewHolder yaitu nama desc dan img
        TextView myText1, myText2;
        ImageView myImage;
        CardView parentLayout;
        //    membuat new viewHolder
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //    itemView. artinya isi per item atau per row
            myText1 = itemView.findViewById(R.id.txtNamaLaundry);
            myText2 = itemView.findViewById(R.id.txtDescriptionLaundry);
            myImage = itemView.findViewById(R.id.imgLogo);
            parentLayout = itemView.findViewById(R.id.parentLayout);

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            final int lightbluex = ContextCompat.getColor(context, R.color.lightbluex);
            final int greyimage = ContextCompat.getColor(context, R.color.greyimage);

            if (sharedPreferences.getBoolean("dark_mode", true)) {
                parentLayout.setCardBackgroundColor(lightbluex);
                myImage.setBackgroundColor(greyimage);
            }

            //respons click on recyclerView
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    userList.get(getAdapterPosition());
//                    Toast.makeText( context, "clicked" + userList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }


}
