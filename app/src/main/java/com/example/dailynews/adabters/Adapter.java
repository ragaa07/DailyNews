package com.example.dailynews.adabters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.dailynews.R;
import com.example.dailynews.models.Articles;
import com.example.dailynews.Utils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
  private   List<Articles>list;
  private Context context;
  private OnItemClickListener onItemClickListener;
    public Adapter(List<Articles> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder((FrameLayout) view,onItemClickListener);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
           final   MyViewHolder holders=holder;
           Articles model=list.get(position);
        RequestOptions requestOptions =new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.centerCrop();
        Glide.with(context)
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade()).into(holder.imageView);
                 holder.title.setText(model.getTitle());
                 holder.desc.setText(model.getDescription());
                 holder.source.setText(model.getSource().getName());
                 holder.time.setText("\u2022"+Utils.DateToTimeFormat(model.getPublishedAt()));
                 holder.publishedAt.setText(Utils.DateFormat(model.getPublishedAt()));
                 holder.author.setText(model.getAuthor());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title,author,publishedAt,desc,time,source;
        ImageView imageView;
        ProgressBar progressBar;

        OnItemClickListener onItemClickListener;
        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            title=itemView.findViewById(R.id.title);
            author=itemView.findViewById(R.id.author);
            publishedAt=itemView.findViewById(R.id.publishedAt);
            desc=itemView.findViewById(R.id.desc);
            time=itemView.findViewById(R.id.time);
            source=itemView.findViewById(R.id.source);
            imageView=itemView.findViewById(R.id.img);
            progressBar=itemView.findViewById(R.id.progress_load_img);
            this.onItemClickListener=onItemClickListener;
        }

        @Override
        public void onClick(View view) {
           onItemClickListener.onItemClick(view,getAdapterPosition());
        }
    }
}
