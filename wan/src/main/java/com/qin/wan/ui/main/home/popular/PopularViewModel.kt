package com.qin.wan.ui.main.home.popular

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.qin.mvvm.base.BaseViewModel
import com.qin.wan.R
import com.qin.mvvm.BR
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.model.bean.Article
import me.tatarka.bindingcollectionadapter2.ItemBinding
import org.json.JSONException

class PopularViewModel : BaseViewModel() {
    // TODO: Implement the ViewModel
    private val repository by lazy { PopularRepository.getInstance(ApiRetrofit.getInstance()) }

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
            items.value = mutableListOf<Article>().apply {
                addAll(it)
            }
            Log.d("ExceptionHandle", items.value?.count().toString())
            !(items.value?.isEmpty() ?: true)
        }, isNotify = isNotify)
    }

    fun loadMoreArticleList() {
        launchOnlyResult({
            repository.getArticleList(page)
        }, {
            page = it.curPage
            val articleList = items.value ?: mutableListOf()
            articleList.addAll(it.datas)
            true
        }, isNotify = false)
    }

    interface OnItemClickListener {
        fun onItemClick(item: Article)
        fun onItemCollectClick(item: Article)
    }
}