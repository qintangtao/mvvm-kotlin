package com.qin.wan.ui.main.home.wechat

import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.BR
import com.qin.mvvm.base.BaseViewModel
import com.qin.mvvm.base.OnItemClickListener
import com.qin.mvvm.event.Message
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.model.bean.Article
import com.qin.wan.model.bean.Category
import com.qin.wan.ui.detail.DetailActivity
import com.qin.wan.ui.main.home.HomeRepository
import me.tatarka.bindingcollectionadapter2.ItemBinding

class WechatViewModel : BaseViewModel() {
    private val repository by lazy { HomeRepository.getInstance(ApiRetrofit.getInstance()) }

    private val itemCategoryOnClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(view: View, item: Category) {
            checkedCat.code = item.id
            _itemsCategory.value = _itemsCategory.value!!.toMutableList()
            refreshWechatList()
        }
    }

    private val itemOnClickListener = object : OnItemClickListener<Article> {
        override fun onItemClick(view: View, item: Article) {
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

    private val _items = MutableLiveData<MutableList<Article>>()
    private val _itemsCategory = MutableLiveData<MutableList<Category>>()

    private var page = 0

    val checkedCat = Message()

    val itemsCategory: LiveData<MutableList<Category>> = _itemsCategory
    val itemBindingCategory = ItemBinding.of<Category>(BR.itemBean, R.layout.item_category_sub)
        .bindExtra(BR.listenner, itemCategoryOnClickListener)
        .bindExtra(BR.checkedCat, checkedCat)

    val items: LiveData<MutableList<Article>> = _items
    val itemBinding = ItemBinding.of<Article>(BR.itemBean, R.layout.item_article)
        .bindExtra(BR.listenner, itemOnClickListener)

    fun getWechatCategories() {
        launchSerialResult({
            repository.getWechatCategories()
        },{
            if (it.isEmpty()) RESULT.EMPTY.code
            else {
                checkedCat.code = it[0].id
                _itemsCategory.value = it
                RESULT.SUCCESS.code
            }
        }, {
            repository.getWechatArticleList(0, checkedCat.code)
        }, {
            if (it.datas.isEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                _items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        })
    }

    fun refreshWechatList(isNotify: Boolean = false) {
        launchOnlyResult({
            repository.getWechatArticleList(0, checkedCat.code)
        }, {
            if (it.datas.isEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                _items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }

    fun loadMoreWechatList() {
        launchOnlyResult({
            repository.getWechatArticleList(page, checkedCat.code)
        }, {
            page = it.curPage
            val list = _items.value ?: mutableListOf<Article>()
            list.addAll(it.datas)
            if (it.offset >= it.total) RESULT.END.code else RESULT.SUCCESS.code
        }, isNotify = false)
    }
}