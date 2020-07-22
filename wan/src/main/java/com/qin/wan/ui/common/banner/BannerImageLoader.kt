package com.qin.wan.ui.common.banner

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.qin.wan.model.bean.Banner
import com.youth.banner.loader.ImageLoader

class BannerImageLoader : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        Glide.with(imageView!!.context)
            .load((path as Banner).imagePath)
            .into(imageView)
    }

}