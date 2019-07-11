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

import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.SavedData;

import java.util.ArrayList;

public class EditAdapter extends RecyclerView.Adapter<EditAdapter.Holder> {
    ArrayList<TimeTable> list;
    Context context;

    public EditAdapter(ArrayList<TimeTable> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_time_table_editer_row,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        final TimeTable timeTable=list.get(i);
        holder.edtTeacher.setText(timeTable.getTeacher());
        holder.edtTime.setText(timeTable.getTime());
        holder.edtSubject.setText(timeTable.getSubject());
        new SavedData(context).log(list.toString());
        holder.btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getAdapterPosition()<getItemCount()-1){
                    notifyItemMoved(holder.getAdapterPosition(),holder.getAdapterPosition()+1);
                    list.add(holder.getAdapterPosition(),list.remove(holder.getAdapterPosition()-1));
                    new SavedData(context).log(list.toString());
                }
            }
        });
        holder.btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getAdapterPosition()>=1){
                    notifyItemMoved(holder.getAdapterPosition(),holder.getAdapterPosition()-1);
                    list.add(holder.getAdapterPosition(),list.remove(holder.getAdapterPosition()+1));
                    new SavedData(context).log(list.toString());
                }
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    list.remove(list.remove(holder.getAdapterPosition()));
                    notifyItemRemoved(holder.getAdapterPosition());
                }catch (Exception e){

                }
            }
        });

        holder.edtSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                timeTable.setSubject(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.edtTeacher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                timeTable.setTeacher(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.edtTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                timeTable.setTime(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        EditText edtSubject,edtTime,edtTeacher;
        ImageButton btnUp,btnDown,btnDelete;
        public Holder(@NonNull View itemView) {
            super(itemView);
            edtSubject=itemView.findViewById(R.id.edtSubject);
            edtTime=itemView.findViewById(R.id.edtTime);
            edtTeacher=itemView.findViewById(R.id.edtTeacher);
            btnUp=itemView.findViewById(R.id.btnUp);
            btnDelete=itemView.findViewById(R.id.btnDelete);
            btnDown=itemView.findViewById(R.id.btnDown);
        }
    }
}
