package com.qin.wan.ui.main.home.popular

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.BR
import com.qin.mvvm.base.BaseViewModel
import com.qin.mvvm.event.Message
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.model.bean.Article
import com.qin.wan.ui.common.OnItemClickListener
import com.qin.wan.ui.detail.DetailActivity
import com.qin.wan.ui.main.home.HomeRepository
import me.tatarka.bindingcollectionadapter2.ItemBinding

class PopularViewModel : BaseViewModel() {
    private val repository by lazy { HomeRepository.getInstance(ApiRetrofit.getInstance()) }

    private val itemOnClickListener = object : OnItemClickListener<Article> {
        override fun onItemClick(view: View, item: Article) {
            //defUI.error.postValue(Message(PopularFragment.START_DETAIL_ARTICLE, obj = item))
            view.context.startActivity(Intent().apply {
                setClass(view.context, DetailActivity::class.java)
                putExtra(DetailActivity.PARAM_ARTICLE, item)
            })
        }

        override fun onItemChildClick(view: View, item: Article) {
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

    var page = 0
    val items = MutableLiveData<MutableList<Article>>()
    var itemBinding = ItemBinding.of<Article>(BR.itemBean, R.layout.item_article)
        .bindExtra(BR.listenner, itemOnClickListener)

    fun refreshArticleList(isNotify: Boolean = false) {
        launchFlowzipResult({
            repository.getTopArticleList()
        }, {
            repository.getArticleList(0)
        }, { l, r ->
            page = r.curPage
            mutableListOf<Article>().apply {
                addAll(l.apply { forEach{it.top = true} })
                addAll(r.datas)
            }
        }, {
            if (it.isEmpty()) RESULT.EMPTY.code
            else {
                items.value = it.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }

    fun loadMoreArticleList() {
        launchOnlyResult({
            repository.getArticleList(page)
        }, {
            page = it.curPage
            val list = items.value ?: mutableListOf<Article>()
            list.addAll(it.datas)
            if (it.offset >= it.total) RESULT.END.code else RESULT.SUCCESS.code
        }, isNotify = false)
    }
}