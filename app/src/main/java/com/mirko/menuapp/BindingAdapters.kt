package com.mirko.menuapp

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorText")
fun setErrorText(inputLayout: TextInputLayout, text: String?) {
    val msg = if (text.isNullOrEmpty()) null else text
    inputLayout.error = msg
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) VISIBLE else INVISIBLE
}

@BindingAdapter("android:src")
fun loadImage(imageView: ImageView, url: String?) {
    Glide.with(imageView)
        .load(url)
        .placeholder(R.drawable.circle_palms)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imageView)
}