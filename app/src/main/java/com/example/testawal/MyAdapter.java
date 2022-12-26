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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    Context context;
    ArrayList<User> userList;
    Dialog myDialog;
    TextView Alamat, Description, Phone, Services, NamaLaundry;
    ImageView iconPopup;
    Button btnPopup;

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

                if(userList.get(viewHolder.getAdapterPosition()).getImageUrl().toString().equals("")){
                    iconPopup.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_profile_icon));
                }else{
                    Glide.with(context).load(userList.get(viewHolder.getAdapterPosition()).getImageUrl()).into(iconPopup);
                }
                NamaLaundry.setText(userList.get(viewHolder.getAdapterPosition()).getNama());
                Phone.setText(userList.get(viewHolder.getAdapterPosition()).getPhone());
                Alamat.setText(userList.get(viewHolder.getAdapterPosition()).getAddress());
                Description.setText(userList.get(viewHolder.getAdapterPosition()).getDescription());
                myDialog.show();
                btnPopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, OrderProcess.class);
                        intent.putExtra("namaLaundry", userList.get(viewHolder.getAdapterPosition()).getNama());
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
