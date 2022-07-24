package com.familyprotection.djasdatabase.Models;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.familyprotection.djasdatabase.R;

import java.util.List;

public class BitmapAdapter extends RecyclerView.Adapter<BitmapAdapter.MyViewHolder> {
    private List<Bitmap> imgList;
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imgItemList);
        }
    }
    public BitmapAdapter(List<Bitmap> imgList) {
        this.imgList = imgList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Bitmap image = imgList.get(position);
        holder.img.setImageBitmap(image);
    }
    @Override
    public int getItemCount() {
        return imgList.size();
    }
}
