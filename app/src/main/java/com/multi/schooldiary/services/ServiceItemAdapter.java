package com.multi.schooldiary.services;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.multi.schooldiary.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.Holder> {
    ArrayList<ServiceItemClass> arrayList;
    Context context;

    public ServiceItemAdapter(ArrayList<ServiceItemClass> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_main_page_row,viewGroup,false);
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,ServiceActivity.class).putExtra("position",String.valueOf(holder.getAdapterPosition())));
            }
        });
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
