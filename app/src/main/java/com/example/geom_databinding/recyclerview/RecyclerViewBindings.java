package com.example.geom_databinding.recyclerview;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.geom_databinding.R;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewBindings {

    @BindingAdapter("setAdapter")
    public static void bindRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

    // 리사이클러뷰 이미지뷰에 이미지 넣기
    @BindingAdapter("setImage")
    public static void bindRecyclerViewAdapter(ImageView imageView, Integer imageUrl){
        if(imageUrl != null){
            if(imageView.getTag(R.id.image_url) == null || !imageView.getTag(R.id.image_url).equals(imageUrl)){
                imageView.setImageBitmap(null);
                imageView.setTag(R.id.image_url, imageUrl);
                Glide.with(imageView).load(imageUrl).into(imageView);
            }
        }

        else {
            imageView.setTag(R.id.image_url, null);
            imageView.setImageBitmap(null);
        }
    }
}
