package com.multi.schooldiary.notice;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multi.schooldiary.R;

import java.util.ArrayList;

abstract class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.Holder> {
    ArrayList<Notice> noticeArrayList;
    Context context;
    String uid;

    public NoticeAdapter(ArrayList<Notice> noticeArrayList, String uid) {
        this.noticeArrayList = noticeArrayList;
        this.uid = uid;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_notice_row,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        final Notice notice=noticeArrayList.get(i);
        holder.tvSignature.setText(notice.getBroadcaster());
        holder.tvDescription.setText(notice.getDescription());
        holder.tvTitle.setText(notice.getTitle());
        holder.tvDate.setText(notice.getDate());
        if(notice.getSubject().trim().length()>0){
            holder.tvSubject.setText("Subject:-"+notice.getSubject());
        }
        if (notice.getUid()!=uid){
            holder.btnEdit.setVisibility(View.VISIBLE);
        }
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener();
                context.startActivity(new Intent(context, NoticeConsoleActivity.class).putExtra("key",notice.getKey()));
            }
        });
    }

    public abstract void onClickListener();

    @Override
    public int getItemCount() {
        return noticeArrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvDate,tvSubject,tvDescription,tvSignature;
        TextView btnEdit;
    public Holder(@NonNull View itemView) {
        super(itemView);
        tvTitle=itemView.findViewById(R.id.tvTitle);
        tvDate=itemView.findViewById(R.id.tvDate);
        tvSubject=itemView.findViewById(R.id.tvSubject);
        tvDescription=itemView.findViewById(R.id.tvDescription);
        tvSignature=itemView.findViewById(R.id.tvSignature);
        btnEdit=itemView.findViewById(R.id.btnEdit);
       }

}
}
