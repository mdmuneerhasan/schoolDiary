package com.multi.schooldiary.teacher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multi.schooldiary.R;

import java.util.ArrayList;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder>{
    ArrayList<String> arrayList;
    Context context;
    public MyAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.item_my_adapter_row,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.tvText.setText(arrayList.get(i));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView tvText;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvText=itemView.findViewById(R.id.tvText);
        }
    }
}
