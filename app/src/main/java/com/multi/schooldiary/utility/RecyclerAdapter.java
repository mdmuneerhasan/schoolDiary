package com.multi.schooldiary.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.multi.schooldiary.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {
    ArrayList<RecyclerClass> arrayList;
    Context context;
    RecyclerClick click;

    public RecyclerAdapter( Context context, RecyclerClick click,ArrayList<RecyclerClass> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        this.click = click;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_manage_class_row,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        RecyclerClass recyclerClass=arrayList.get(i);
        holder.tvHeading.setText(recyclerClass.getHeading());
        holder.text1.setText(recyclerClass.getText1());
        holder.text2.setText(recyclerClass.getText2());
        holder.text3.setText(recyclerClass.getText3());
        holder.text4.setText(recyclerClass.getText4());
        holder.tvNotification.setText(recyclerClass.getTvNotification());
        try {
            Picasso.get().load(recyclerClass.getUrl()).into(holder.imageView);
        }catch (Exception e){}
        click.onBindViewHolder(holder,arrayList.get(i));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView text1,text2,text3,text4,tvNotification,tvHeading;
        public Button btn1,btn2,btn3,btn4;
        Spinner spinner;
        RelativeLayout relativeLayout;
        public Holder(@NonNull View itemView) {
            super(itemView);
            relativeLayout=itemView.findViewById(R.id.layout);
            spinner=itemView.findViewById(R.id.spinner);
            imageView=itemView.findViewById(R.id.imageView);
            tvHeading=itemView.findViewById(R.id.tvHeading);
            text1=itemView.findViewById(R.id.tvText1);
            text2=itemView.findViewById(R.id.tvText2);
            text3=itemView.findViewById(R.id.tvText3);
            text4=itemView.findViewById(R.id.tvText4);
            btn1=itemView.findViewById(R.id.btn1);
            btn2=itemView.findViewById(R.id.btn2);
            btn3=itemView.findViewById(R.id.btn3);
            btn4=itemView.findViewById(R.id.btn4);
            tvNotification=itemView.findViewById(R.id.tvNotification);
            imageView.setVisibility(View.GONE);
            tvHeading.setVisibility(View.GONE);
            text1.setVisibility(View.GONE);
            text2.setVisibility(View.GONE);
            text3.setVisibility(View.GONE);
            text4.setVisibility(View.GONE);
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            btn4.setVisibility(View.GONE);
            tvNotification.setVisibility(View.GONE);
        }
    }
}
