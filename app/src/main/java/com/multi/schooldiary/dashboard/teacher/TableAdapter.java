package com.multi.schooldiary.dashboard.teacher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.RecyclerClass;
import java.util.ArrayList;

class TableAdapter  extends RecyclerView.Adapter<TableAdapter.Holder> {
    ArrayList<RecyclerClass> list;
    Context context;
    TableAdapterClick click;
    public TableAdapter(ArrayList<RecyclerClass> list,TableAdapterClick click) {
        this.list = list;
        this.click=click;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        return new Holder(layoutInflater.inflate(R.layout.item_table_adapter_row,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        final RecyclerClass recyclerClass=list.get(i);
        holder.tvText1.setText(recyclerClass.getText1());
        holder.tvText2.setText(recyclerClass.getText2());
        holder.tvText3.setText(recyclerClass.getText3());
        holder.tvText4.setText(recyclerClass.getText4());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(new CharSequence[]
                                {"present", "absent", "second half-present", "second half-absent"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                click.switchTo(which,holder,recyclerClass);
                            }
                        });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView tvText1,tvText2,tvText3,tvText4;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvText1=itemView.findViewById(R.id.tvText1);
            tvText2=itemView.findViewById(R.id.tvText2);
            tvText3=itemView.findViewById(R.id.tvText3);
            tvText4=itemView.findViewById(R.id.tvText4);
        }
    }
}
