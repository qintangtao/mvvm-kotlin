package com.kotlin.wanandroid.ui.main.home.plaza

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
import com.kotlin.wanandroid.ui.main.home.HomeRepository
import com.kotlin.wanandroid.ui.detail.DetailActivity
import me.tatarka.bindingcollectionadapter2.ItemBinding

class PlazaViewModel : BaseViewModel() {

    private val repository by lazy { HomeRepository.getInstance(ApiRetrofit.getInstance()) }

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

    private var page = 0

    private val _items = MutableLiveData<MutableList<Article>>()

    val items: LiveData<MutableList<Article>> = _items
    val itemBinding = ItemBinding.of<Article>(BR.itemBean, R.layout.item_article_simple)
        .bindExtra(BR.listenner, itemOnClickListener)

    fun refreshUserArticleList(isNotify: Boolean = false) {
        launchOnlyResult({
            repository.getUserArticleList(0)
        }, {
            if (it.datas.isEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                _items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }

    fun loadMoreUserArticleList() {
        launchOnlyResult({
            repository.getUserArticleList(page)
        }, {
            page = it.curPage
            val list = _items.value ?: mutableListOf<Article>()
            list.addAll(it.datas)
            if (it.offset >= it.total) RESULT.END.code else RESULT.SUCCESS.code
        }, isNotify = false)
    }
}