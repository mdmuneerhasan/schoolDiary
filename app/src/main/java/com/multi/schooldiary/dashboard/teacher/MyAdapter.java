package com.multi.schooldiary.dashboard.teacher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multi.schooldiary.R;

import java.util.ArrayList;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder>{
    ArrayList<String> arrayList;
    Context context;
    MyAdapterClick click;
    public MyAdapter(ArrayList<String> arrayList, MyAdapterClick click) {
        this.arrayList = arrayList;
        this.click = click;
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
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        holder.tvText.setText(arrayList.get(i));
        click.onBindViewHolder(holder,arrayList.get(i));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

public class Holder extends RecyclerView.ViewHolder{
        TextView tvText;
        LinearLayout linearLayout;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvText=itemView.findViewById(R.id.tvText);
            linearLayout=itemView.findViewById(R.id.linearLayout1);
        }
    }
}
