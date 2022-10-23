package com.example.testawal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    Context context;
    ArrayList<User> userList;

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
        //saat dipanggil berulang-ulang akan return ke new ViewHolder
        //akan memanggil MyViewHolder
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        //isi dulu attribute myViewHolder
        //saat tarik viewHolder, kita isi di posisi berapa

        Glide.with(context).load(userList.get(position).getImageUrl()).into(holder.myImage);
        holder.myText1.setText(userList.get(position).getNama());
        holder.myText2.setText(userList.get(position).getRole());
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
        //    membuat new viewHolder
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //    itemView. artinya isi per item atau per row
            myText1 = itemView.findViewById(R.id.txtNamaLaundry);
            myText2 = itemView.findViewById(R.id.txtDescriptionLaundry);
            myImage = itemView.findViewById(R.id.imgLogo);
        }
    }


}
