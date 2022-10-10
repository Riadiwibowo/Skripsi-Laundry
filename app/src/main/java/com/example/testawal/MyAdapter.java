package com.example.testawal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    Context context;
//    int images[];
    ArrayList<Laundry> laundryList;

    public MyAdapter(Context context, ArrayList<Laundry> laundryList) {
        this.context = context;
        this.laundryList = laundryList;
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
//        holder.myText1.setText(data1[position]);
//        holder.myText2.setText(data2[position]);
//        holder.myImage.setImageResource((images[position]));
        Laundry laundry = laundryList.get(position);
        holder.myText1.setText(laundry.getNamaLaundry());
        holder.myText2.setText(laundry.getDeskripsiLaundry());
//        holder.myImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return laundryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //    isi dari viewHolder yaitu nama desc dan img
        TextView myText1, myText2;
//        ImageView myImage;
        //    membuat new viewHolder
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        //    itemView. artinya isi per item atau per row
            myText1 = itemView.findViewById(R.id.txtNamaLaundry);
            myText2 = itemView.findViewById(R.id.txtDescriptionLaundry);
//            myImage = itemView.findViewById(R.id.imgLogo);
        }
    }


}
