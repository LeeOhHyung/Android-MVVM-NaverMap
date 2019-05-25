package com.example.geom_databinding.recyclerview;

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.geom_databinding.R

object RecyclerViewBindings {

    @JvmStatic
    @BindingAdapter("setAdapter")
    fun bindRecyclerViewAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>){
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
    }


    // 리사이클러뷰 이미지뷰에 이미지 넣기
    @JvmStatic
    @BindingAdapter("setImage")
    fun bindRecyclerViewAdapter(imageView: ImageView, imageUrl: Integer){
        if(imageView.getTag(R.id.image_url) == null || (imageView.getTag(R.id.image_url) != imageUrl)){
            imageView.setImageBitmap(null)
            imageView.setTag(R.id.image_url, imageUrl)
            Glide.with(imageView).load(imageUrl).into(imageView)
        }
    }
}
