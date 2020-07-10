package com.qin.wan.ui.main.home.wechat

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
import com.qin.wan.model.bean.Category
import com.qin.wan.ui.common.OnItemClickListener
import com.qin.wan.ui.main.home.HomeRepository
import me.tatarka.bindingcollectionadapter2.ItemBinding

class WechatViewModel : BaseViewModel() {
    private val repository by lazy { HomeRepository.getInstance(ApiRetrofit.getInstance()) }

    private val itemCategoryOnClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(item: Category) {
            checkedCid = item.id
            val list = itemsCategory.value
            list?.let {
                itemsCategory.value = mutableListOf<Category>().apply {
                    addAll(it)
                }
            }
            refreshWechatList()
        }
        override fun onItemChildClick(view: View, item: Category) {}
    }

    private val itemOnClickListener = object : OnItemClickListener<Article> {
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


    var checkedCid = 0
    val itemsCategory = MutableLiveData<MutableList<Category>>()
    val itemBindingCategory = ItemBinding.of<Category>(BR.itemBean, R.layout.item_category_sub)
        .bindExtra(BR.listenner, itemCategoryOnClickListener)
        .bindExtra(BR.checkedCid, checkedCid)

    val items = MutableLiveData<MutableList<Article>>()
    val itemBinding = ItemBinding.of<Article>(BR.itemBean, R.layout.item_article)
        .bindExtra(BR.listenner, itemOnClickListener)

    var page = 0

    fun getWechatCategories() {
        launchFlowResult({
            repository.getWechatCategories()
        },{
            if (it.isEmpty()) RESULT.EMPTY.code
            else {
                checkedCid = it[0].id
                itemsCategory.postValue(it)
                RESULT.SUCCESS.code
            }
        }, {
            repository.getWechatArticleList(0, checkedCid)
        }, {
            if (it.datas.isEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        })
/*
        launchOnlyResult3({
            repository.getProjectCategories()
        }, {
            checkedCid = it[0].id
            Log.d("checkedCid", "checkedCid: " + checkedCid)
            itemsCategory.value = it
            if (it.isEmpty()) RESULT.EMPTY.code else RESULT.SUCCESS.code
        }, {
            repository.getProjectListByCid(0, checkedCid)
        }, {
            items.value = it.datas.toMutableList()
            if (!(items.value?.isEmpty() ?: true)) RESULT.SUCCESS.code else RESULT.EMPTY.code
        })*/
    }

    fun refreshWechatList(isNotify: Boolean = false) {
        launchOnlyResult({
            repository.getWechatArticleList(0, checkedCid)
        }, {
            if (it.datas.isEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }

    fun loadMoreWechatList() {
        launchOnlyResult({
            repository.getWechatArticleList(page, checkedCid)
        }, {
            page = it.curPage
            val list = items.value ?: mutableListOf<Article>()
            list.addAll(it.datas)
            if (it.offset >= it.total) RESULT.END.code else RESULT.SUCCESS.code
        }, isNotify = false)
    }
}