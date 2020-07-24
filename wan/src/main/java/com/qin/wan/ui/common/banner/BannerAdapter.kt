package com.qin.wan.ui.common.banner

import androidx.databinding.BindingAdapter
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.listener.OnBannerListener
import com.zhy.view.flowlayout.TagFlowLayout

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

    @JvmStatic
    @BindingAdapter(value = ["onItemClick"], requireAll = false)
    fun setOnBannerListener(banner: Banner, listener: OnBannerListener) {
        banner.setOnBannerListener(listener)
    }
}