package com.multi.schooldiary.timetable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.multi.schooldiary.R;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.Holder> {
    ArrayList<TimeTable> list;
    Context context;

    public TimeAdapter(ArrayList<TimeTable> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_time_table_row,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        final TimeTable timeTable=list.get(i);
        if(timeTable.getTeacher()!=null && timeTable.getTeacher().trim().length()>0){
            holder.edtTeacher.setText("Taught by : "+timeTable.getTeacher());
        }
        holder.edtTime.setText(timeTable.getTime());
        holder.edtSubject.setText(timeTable.getSubject());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView edtSubject,edtTime,edtTeacher;
        public Holder(@NonNull View itemView) {
            super(itemView);
            edtSubject=itemView.findViewById(R.id.edtSubject);
            edtTime=itemView.findViewById(R.id.edtTime);
            edtTeacher=itemView.findViewById(R.id.edtTeacher);
        }
    }
}
