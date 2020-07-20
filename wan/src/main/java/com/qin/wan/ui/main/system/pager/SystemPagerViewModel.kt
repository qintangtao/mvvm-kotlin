package com.qin.wan.ui.main.system.pager

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
import com.qin.wan.model.bean.Category
import com.qin.wan.ui.common.OnItemClickListener
import com.qin.wan.ui.detail.DetailActivity
import com.qin.wan.ui.main.system.SystemRepository
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SystemPagerViewModel : BaseViewModel() {

    private val repository by lazy { SystemRepository.getInstance(ApiRetrofit.getInstance()) }

    private val itemCategoryOnClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(view: View, item: Category) {
            checkedCat.code = item.id
            val list = itemsCategory.value
            list?.let {
                itemsCategory.value = mutableListOf<Category>().apply {
                    addAll(it)
                }
            }
            defUI.result.postValue(10000)
            refreshArticleList()
        }
        override fun onItemChildClick(view: View, item: Category) {}
    }

    private val itemOnClickListener = object : OnItemClickListener<Article> {
        override fun onItemClick(view: View, item: Article) {
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
    var checkedCat = Message()

    val itemsCategory = MutableLiveData<MutableList<Category>>()
    val itemBindingCategory = ItemBinding.of<Category>(BR.itemBean, R.layout.item_category_sub)
        .bindExtra(BR.listenner, itemCategoryOnClickListener)
        .bindExtra(BR.checkedCat, checkedCat)

    val items = MutableLiveData<MutableList<Article>>()
    val itemBinding = ItemBinding.of<Article>(BR.itemBean, R.layout.item_article)
        .bindExtra(BR.listenner, itemOnClickListener)


    fun getArticleList(categorys: ArrayList<Category>) {
        if (categorys.isEmpty()) {
            defUI.result.postValue(RESULT.EMPTY.code)
            return
        }
        checkedCat.code = categorys[0].id
        itemsCategory.value = categorys.toMutableList()

        launchOnlyResult({
            repository.getArticleListByCid(0, checkedCat.code)
        }, {
            if (it.datas.isEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        })
    }

    fun refreshArticleList(isNotify: Boolean = false) {
        launchOnlyResult({
            repository.getArticleListByCid(0, checkedCat.code)
        }, {
            if (it.datas.isEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }

    fun loadMoreArticleList() {
        launchOnlyResult({
            repository.getArticleListByCid(page, checkedCat.code)
        }, {
            page = it.curPage
            val list = items.value ?: mutableListOf<Article>()
            list.addAll(it.datas)
            if (it.offset >= it.total) RESULT.END.code else RESULT.SUCCESS.code
        }, isNotify = false)
    }
}