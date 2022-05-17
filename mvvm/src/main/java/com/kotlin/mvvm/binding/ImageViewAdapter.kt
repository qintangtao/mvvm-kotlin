package com.kotlin.mvvm.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object ImageViewAdapter {

    @JvmStatic
    @BindingAdapter(value = ["url", "placeholder"], requireAll = false)
    fun setImageUrl(imageView: ImageView, url: String, placeholder: Int) {
        Glide.with(imageView.context)
            .load(url)
            .apply(RequestOptions().placeholder(placeholder))
            .into(imageView)
    }

}