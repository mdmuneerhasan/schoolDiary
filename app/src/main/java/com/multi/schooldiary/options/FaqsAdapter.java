package com.multi.schooldiary.options;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;

import java.util.ArrayList;

abstract class FaqsAdapter extends RecyclerView.Adapter<FaqsAdapter.Holder> {
    ArrayList<Faqs> arrayList;
    Context context;
    String uid;
    SavedData savedData;
    Connection connection;
    public FaqsAdapter(ArrayList<Faqs> arrayList, String uid) {
        this.arrayList = arrayList;
        this.uid = uid;
        connection=new Connection();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        savedData =new SavedData(context);
        View view= LayoutInflater.from(context).inflate(R.layout.item_faqs_row,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        final Faqs faqs=arrayList.get(i);
        holder.tvQuestion.setText(faqs.getQuestion());
        holder.tvAsker.setText(faqs.getAsker());
        holder.tvAnswer.setText(faqs.getAnswer());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uid.equals(faqs.getUid())){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Ask Question");
                    final EditText input = new EditText(context);
                    input.setText(faqs.getQuestion());
                    alertDialog.setView(input);
                    alertDialog.setPositiveButton("ask",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    faqs.setQuestion(input.getText().toString());
                                    faqs.setAnswer("we will response you back soon");
                                    connection.getFaqs().child(faqs.getKey()).setValue(faqs);
                                    onStartAgain();
                                }
                            });
                    alertDialog.setNegativeButton("delete",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    connection.getFaqs().child(faqs.getKey()).removeValue();
                                    onStartAgain();
                                }
                            });
                    alertDialog.show();

                }
            }
        });
    }

    public abstract void onStartAgain();

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView tvAsker,tvQuestion,tvAnswer;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvAnswer=itemView.findViewById(R.id.tvAnswer);
            tvAsker=itemView.findViewById(R.id.tvAsker);
            tvQuestion=itemView.findViewById(R.id.tvQuestion);
        }
    }
}
