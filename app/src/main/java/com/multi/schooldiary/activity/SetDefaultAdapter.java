package com.multi.schooldiary.activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.SetDefaultClass;
import com.multi.schooldiary.utility.SavedData;

import java.util.ArrayList;

public class SetDefaultAdapter extends RecyclerView.Adapter<SetDefaultAdapter.Holder> {
    ArrayList<SetDefaultClass> list;
    Context context;
    SavedData savedData;
    Click click;
    public SetDefaultAdapter(Context context, Click click,ArrayList<SetDefaultClass> list) {
        this.list = list;
        this.click=click;
        this.context = context;
        savedData =new SavedData(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.item_set_default_row,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        click.onBindViewHolder(holder,list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        public TextView btn1,tvName,tvPosition;
        public Button btn2,btn3,btn4;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvPosition=itemView.findViewById(R.id.tvPosition);
            btn1=itemView.findViewById(R.id.btn1);
            btn2=itemView.findViewById(R.id.btn2);
            btn3=itemView.findViewById(R.id.btn3);
            btn4=itemView.findViewById(R.id.btn4);
        }
    }
}
