package com.moong.programers.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.moong.programers.data.ItemData
import com.moong.programers.main.ItemAdapter


object BindAdapter {
    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
    fun bindImage(imageView: ImageView, url: String?, placeholder: Drawable?) {
        Glide.with(imageView.context).load(url).apply(RequestOptions.placeholderOf(placeholder)).into(imageView)
    }
    @JvmStatic
    @BindingAdapter(value = ["circleImageUrl", "placeHolder"], requireAll = false)
    fun bindCircleImage(imageView: ImageView, url: String?, placeholder: Drawable?) {
        Glide.with(imageView.context).load(url).apply(RequestOptions.circleCropTransform().placeholder(placeholder)).into(imageView)
    }
    @JvmStatic
    @BindingAdapter("items")
    fun bindItems(recyclerView: RecyclerView, list: List<ItemData>) {
        val adapter = recyclerView.adapter ?: return
        if (adapter is ItemAdapter) {
            if(adapter.itemCount>0){
                adapter.addItems(list)
            }else{
                adapter.setItems(list)
            }
        }
    }
}