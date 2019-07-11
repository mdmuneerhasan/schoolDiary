package com.multi.schooldiary.school;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.multi.schooldiary.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.Holder> {
    ArrayList<Upload> arrayList;
    String uid;
    Context context;
    public UploadAdapter(ArrayList<Upload> arrayList, String uid) {
        this.arrayList = arrayList;
        this.uid = uid;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_upload_row,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        final Upload upload=arrayList.get(i);
        holder.tvTitle.setText(upload.getTitle());
        holder.tvSchoolName.setText(upload.getSchoolName());
        holder.tvDescription.setText(upload.getDescription());
        holder.tvTime.setText(upload.getTime());
        if(upload.getLikeCount()!=null){
            holder.tvLikeCount.setText(upload.getLikeCount()+" Likes");
        }
        holder.tvUploaderName.setText(upload.getUploaderName());
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like(upload.getKey(),holder.tvLikeCount,upload.getLikeCount());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment(upload.getKey());
            }
        });
        if(uid.equals(upload.getUid())){
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    delete(upload.getKey(),upload.getImageUri(),holder.getAdapterPosition());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });
        }else {
            holder.btnEdit.setVisibility(View.INVISIBLE);
        }
        if(upload.getImageUri()!=null){
            Picasso.get().load(upload.getImageUri()).into(holder.imageView2, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    holder.progressBar.setVisibility(View.GONE);
                }
            });
        }else {
            holder.progressBar.setVisibility(View.GONE);
         holder.imageView2.setVisibility(View.GONE);
        }
        if(upload.getSchoolPhoto()!=null){
            Picasso.get().load(upload.getSchoolPhoto()).into(holder.imgSchool);
        }else {
                Picasso.get().load("https://previews.123rf.com/images/bsd555/bsd5551709/bsd555170901068/86916357-diary-notebook-flat-design-long-shadow-glyph-icon-school-journal-with-bookmark-vector-silhouette-ill.jpg").into(holder.imgSchool);
        }
    }

    public void like(String key, TextView tvLikeCount, String likeCount) {
    }

    public void comment(String key) {
    }

    public void delete(String uploadKey, String uri, int adapterPosition) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView imgSchool,imageView2,btnEdit,btnLike;
        TextView tvTitle,tvSchoolName,tvDescription,tvTime,tvLikeCount,tvUploaderName;
        ProgressBar progressBar;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imgSchool=itemView.findViewById(R.id.imageView);
            imageView2=itemView.findViewById(R.id.imageView2);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvSchoolName=itemView.findViewById(R.id.tvSchoolName);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            tvUploaderName=itemView.findViewById(R.id.uploaderName);
            tvTime=itemView.findViewById(R.id.tvTime);
            btnEdit=itemView.findViewById(R.id.btnEdit);
            btnLike=itemView.findViewById(R.id.imageViewLike);
            tvLikeCount=itemView.findViewById(R.id.tvCountLike);
            progressBar=itemView.findViewById(R.id.progress_circular);
        }
    }
}
