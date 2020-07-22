package com.qin.wan.ui.common.banner

import androidx.databinding.BindingAdapter
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer

object BannerAdapter {

    @JvmStatic
    @BindingAdapter(value = ["images"], requireAll = false)
    fun setImages(banner: Banner, images: List<Any>?) {
        images?.let {
            banner.run {
                setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                setImageLoader(BannerImageLoader())
                setImages(it)
                //setBannerAnimation(Transformer.BackgroundToForeground)
                start()
            }
        }
    }

}