package com.kotlin.wanandroid.ui.search.result

import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kotlin.mvvm.BR
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.base.OnItemClickListener
import com.kotlin.mvvm.network.RESULT
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.model.api.ApiRetrofit
import com.kotlin.wanandroid.model.bean.Article
import com.kotlin.wanandroid.ui.detail.DetailActivity
import com.kotlin.wanandroid.ui.search.SearchRepository
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SearchResultViewModel : BaseViewModel() {

    private val repository by lazy { SearchRepository.getInstance(ApiRetrofit.getInstance()) }

    private val itemOnClickListener = object : OnItemClickListener<Article> {
        override fun onClick(view: View, item: Article) {
            when(view.id) {
                R.id.iv_collect -> {
                    item.collect = !item.collect
                    _items.value = _items.value!!.toMutableList()
                }
                else -> {
                    view.context.startActivity(Intent().apply {
                        setClass(view.context, DetailActivity::class.java)
                        putExtra(DetailActivity.PARAM_ARTICLE, item)
                    })
                }
            }
        }
    }

    private var currentKeywords = ""
    private var page = 0

    private val _items = MutableLiveData<MutableList<Article>>()

    val items : LiveData<MutableList<Article>> = _items
    val itemBinding = ItemBinding.of<Article>(BR.itemBean, R.layout.item_article)
        .bindExtra(BR.listenner, itemOnClickListener)

    fun search(keywords: String = currentKeywords, isNotify: Boolean = false) {
        if (currentKeywords != keywords) {
            currentKeywords = keywords
            _items.value = emptyList<Article>().toMutableList()
        }

        launchOnlyResult({
            repository.search(keywords, 0)
        }, {
            if (it.datas.isNullOrEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                _items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }

    fun loadMore() {
        launchOnlyResult({
            repository.search(currentKeywords, page)
        }, {
            page = it.curPage
            val list = _items.value ?: mutableListOf<Article>()
            list.addAll(it.datas)
            if (it.offset >= it.total) RESULT.END.code else RESULT.SUCCESS.code
        }, isNotify = false)
    }
}