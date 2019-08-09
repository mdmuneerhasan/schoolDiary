package com.multi.schooldiary.services.shop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.multi.schooldiary.R;
import com.multi.schooldiary.services.ServiceItemClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.Holder> {
    ArrayList<ServiceItemClass> arrayList;
    Context context;

    public ShopItemAdapter(ArrayList<ServiceItemClass> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_shop_row,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        ServiceItemClass itemClass=arrayList.get(i);
        holder.tvName.setText(itemClass.getTitle());
        holder.tvDescription.setText(itemClass.getDescription());
        holder.tvPrice.setText(itemClass.getPrice());
        try {
            Picasso.get().load(itemClass.getUrl()).into(holder.imageView);
        }catch (Exception e){
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tvName,tvDescription,tvPrice;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            tvName=itemView.findViewById(R.id.tvTitle);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            tvPrice=itemView.findViewById(R.id.tvPrice);
        }
    }
}
