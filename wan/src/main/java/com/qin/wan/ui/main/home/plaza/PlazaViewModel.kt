package com.qin.wan.ui.main.home.plaza

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.qin.mvvm.BR
import com.qin.mvvm.base.BaseViewModel
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.model.bean.Article
import com.qin.wan.ui.main.home.HomeRepository
import com.qin.wan.ui.common.OnItemClickListener
import me.tatarka.bindingcollectionadapter2.ItemBinding

class PlazaViewModel : BaseViewModel() {

    private val repository by lazy { HomeRepository.getInstance(ApiRetrofit.getInstance()) }

    private val itemOnClickListener = object :
        OnItemClickListener<Article> {
        override fun onItemClick(item: Article) {
            ToastUtils.showLong(item.author)
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

    val items = MutableLiveData<MutableList<Article>>()
    var itemBinding = ItemBinding.of<Article>(BR.itemBean, R.layout.item_article_simple)
        .bindExtra(BR.listenner, itemOnClickListener)

    var page = 0

    fun refreshUserArticleList(isNotify: Boolean = false) {
        launchOnlyResult({
            repository.getUserArticleList(0)
        }, {
            if (it.datas.isEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }

    fun loadMoreUserArticleList() {
        launchOnlyResult({
            repository.getUserArticleList(page)
        }, {
            page = it.curPage
            val list = items.value ?: mutableListOf<Article>()
            list.addAll(it.datas)
            if (it.offset >= it.total) RESULT.END.code else RESULT.SUCCESS.code
        }, isNotify = false)
    }
}