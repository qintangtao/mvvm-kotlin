package com.qin.wan.ui.main.discovery

import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.BR
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.model.bean.Article
import com.qin.wan.model.bean.Banner
import com.qin.wan.model.bean.Frequently
import com.qin.wan.model.bean.HotWord
import com.qin.wan.ui.base.BaseUserViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding


class DiscoveryViewModel : BaseUserViewModel() {

    private val disRepository by lazy { DiscoveryRepository.getInstance(ApiRetrofit.getInstance()) }

    val banners = MutableLiveData<MutableList<Banner>>()

    val hotWordItems = MutableLiveData<MutableList<HotWord>>()
    val hotWordItemBinding = ItemBinding.of<HotWord>(BR.itemBean, R.layout.item_hot_word)

    val frequentlyItems = MutableLiveData<MutableList<Frequently>>()
    val frequentlyItemBinding = com.qin.mvvm.binding.adapter
        .ItemBinding.of<Frequently>(BR.itemBean, R.layout.item_nav_tag)


    fun getData(isNotify: Boolean = false) {
        if (isNotify) callStart()
        launchOnlyResult({
            disRepository.getBanners()
        },{
            if (it.isNullOrEmpty()) RESULT.EMPTY.code
            else {
                banners.value = it.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = false)

        launchOnlyResult({
            disRepository.getHotWords()
        },{
            if (it.isNullOrEmpty()) RESULT.EMPTY.code
            else {
                hotWordItems.value = it.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = false)

        launchOnlyResult({
            disRepository.getFrequentlyWebsites()
        },{
            if (it.isNullOrEmpty()) RESULT.EMPTY.code
            else {
                frequentlyItems.value = it.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = false)
    }
}