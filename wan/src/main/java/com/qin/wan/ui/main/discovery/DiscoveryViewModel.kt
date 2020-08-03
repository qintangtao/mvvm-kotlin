package com.qin.wan.ui.main.discovery

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.qin.mvvm.BR
import com.qin.mvvm.base.OnItemClickListener
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.model.bean.Article
import com.qin.wan.model.bean.Banner
import com.qin.wan.model.bean.Frequently
import com.qin.wan.model.bean.HotWord
import com.qin.wan.ui.base.BaseUserViewModel
import com.qin.wan.ui.detail.DetailActivity
import com.youth.banner.listener.OnBannerListener
import com.zhy.view.flowlayout.TagFlowLayout
import me.tatarka.bindingcollectionadapter2.ItemBinding


class DiscoveryViewModel : BaseUserViewModel() {

    private val disRepository by lazy { DiscoveryRepository.getInstance(ApiRetrofit.getInstance()) }

    private val itemOnClickListener = object : OnItemClickListener<HotWord> {
        override fun onClick(view: View, item: HotWord) {
            Log.d("onClick", item.name + " ===  " + item.link)
            view.context.startActivity(Intent().apply {
                setClass(view.context, DetailActivity::class.java)
                putExtra(DetailActivity.PARAM_ARTICLE, Article(
                    title = item.name, link = item.link))
            })
        }
    }

    private val _banners = MutableLiveData<MutableList<Banner>>()
    private val _hotWordItems = MutableLiveData<MutableList<HotWord>>()
    private val _frequentlyItems = MutableLiveData<MutableList<Frequently>>()

    val banners : LiveData<MutableList<Banner>> = _banners

    val hotWordItems : LiveData<MutableList<HotWord>> = _hotWordItems
    val hotWordItemBinding = ItemBinding.of<HotWord>(BR.itemBean, R.layout.item_hot_word)
        .bindExtra(BR.listenner, itemOnClickListener)

    val frequentlyItems : LiveData<MutableList<Frequently>> = _frequentlyItems
    val frequentlyItemBinding = com.qin.mvvm.binding.adapter
        .ItemBinding.of<Frequently>(BR.itemBean, R.layout.item_frequently_tag)


    val onBannerClickListener = OnBannerListener { position ->
        val banner =  _banners.value!![position]
        ActivityUtils.startActivity(
            Intent().apply {
                setClass(ActivityUtils.getTopActivity(), DetailActivity::class.java)
                putExtra(DetailActivity.PARAM_ARTICLE, Article(
                    title = banner.title, link = banner.url))
            }
        )
    }

    val onTagClickListener = TagFlowLayout.OnTagClickListener { view, position, _ ->
        val frequently =  _frequentlyItems.value!![position]
        view.context.startActivity(Intent().apply {
            setClass(view.context, DetailActivity::class.java)
            putExtra(DetailActivity.PARAM_ARTICLE, Article(
                title = frequently.name, link = frequently.link))
        })
        true
    }

    fun getData(isNotify: Boolean = false) {
        launchFlowCombine3Result({
            disRepository.getBanners()
        },{
            disRepository.getHotWords()
        }, {
            disRepository.getFrequentlyWebsites()
        }, {
            val a = it[0] as List<Banner>
            val b = it[1] as List<HotWord>
            val c = it[2] as List<Frequently>
            if (a.isNullOrEmpty() || b.isNullOrEmpty() || c.isNullOrEmpty()) {
                RESULT.EMPTY.code
            } else {
                _banners.value = a.toMutableList()
                _hotWordItems.value = b.toMutableList()
                _frequentlyItems.value = c.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }
}