package com.familyprotection.djasdatabase.Models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.familyprotection.djasdatabase.R;

import java.util.List;

public class BitmapAdapter extends RecyclerView.Adapter<BitmapAdapter.MyViewHolder> {
    private final List<Bitmap> imgList;
    private final ItemClickListener itemClickListener;
    public int selected_position = -1;
    public int previous_position = -1;
    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        LinearLayout backgroundImg;
        MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imgItemList);
            backgroundImg = view.findViewById(R.id.imgItemSelection);
        }

        void defaultBg() {
            Drawable drawable = AppCompatResources.getDrawable(backgroundImg.getContext(), R.color.black);
            backgroundImg.setBackground(drawable);
        }

        void selectedBg() {
            Drawable drawable = AppCompatResources.getDrawable(backgroundImg.getContext(), R.drawable.rectangle_background_selected_shape);
            backgroundImg.setBackground(drawable);
        }
    }
    public BitmapAdapter(List<Bitmap> imgList, ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
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
        if (position == selected_position) {
            holder.selectedBg();
        } else {
            holder.defaultBg();
        }
        holder.itemView.setOnClickListener(view -> itemClickListener.onItemClick(image, holder.getAdapterPosition()));
    }
    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public interface ItemClickListener{
        void onItemClick(Bitmap image, int position);
    }


}
