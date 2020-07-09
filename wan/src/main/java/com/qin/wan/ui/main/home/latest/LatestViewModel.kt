package com.qin.wan.ui.main.home.latest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.qin.mvvm.BR
import com.qin.mvvm.base.BaseViewModel
import com.qin.wan.R
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.model.bean.Article
import com.qin.wan.ui.main.home.OnItemClickListener
import me.tatarka.bindingcollectionadapter2.ItemBinding

class LatestViewModel : BaseViewModel() {

    private val repository by lazy { LatestRepository.getInstance(ApiRetrofit.getInstance()) }

    private val itemOnClickListener = object : OnItemClickListener {
        override fun onItemClick(item: Article) {
            ToastUtils.showLong(item.author)
        }

        override fun onItemCollectClick(item: Article) {
            Log.d("ImageAdapter", "id:${item.id} ,collect:${item.collect}")
            val list = items.value
            val item2 = list?.find { it.id == item.id } ?: return
            item2.collect = !item2.collect
            //items.value = list
            items.value = mutableListOf<Article>().apply {
                addAll(list)
            }
            Log.d("ImageAdapter", "id:${item.id} ,collect:${item.collect}")
        }
    }

    val items = MutableLiveData<MutableList<Article>>()
    var itemBinding = ItemBinding.of<Article>(BR.itemBean, R.layout.item_article)
        .bindExtra(BR.listenner, itemOnClickListener)

    var page = 0

    fun refreshProjectList(isNotify: Boolean = false) {
        launchOnlyResult({
            repository.getProjectList(0)
        }, {
            page = it.curPage
            items.value = it.datas.toMutableList()
            true
        }, isNotify = false)
    }

    fun loadMoreProjectList() {
        launchOnlyResult({
            repository.getProjectList(page)
        }, {
            page = it.curPage
            val articleList = items.value ?: mutableListOf()
            articleList.addAll(it.datas)
            true
        }, isNotify = false)
    }
}