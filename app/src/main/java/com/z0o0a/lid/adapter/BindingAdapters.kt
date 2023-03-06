package com.z0o0a.lid.adapter

import android.graphics.Bitmap

import android.widget.ImageView

import androidx.databinding.BindingAdapter


class BindingAdapters {
    // imageview databinding
    @BindingAdapter("imageBitmap")
    fun loadImage(imageView: ImageView, bitmap: Bitmap?) {
        imageView.setImageBitmap(bitmap)
    }
}