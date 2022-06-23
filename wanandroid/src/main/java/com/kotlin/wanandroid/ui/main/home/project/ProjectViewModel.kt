package com.kotlin.wanandroid.ui.main.home.project

import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kotlin.mvvm.BR
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.base.OnItemClickListener
import com.kotlin.mvvm.binding.adapter.ItemBinding
import com.kotlin.mvvm.event.Message
import com.kotlin.mvvm.network.RESULT
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.model.api.ApiRetrofit
import com.kotlin.wanandroid.model.bean.Article
import com.kotlin.wanandroid.model.bean.Category
import com.kotlin.wanandroid.ui.main.home.HomeRepository
import com.kotlin.wanandroid.ui.detail.DetailActivity

class ProjectViewModel : BaseViewModel() {
    private val repository by lazy { HomeRepository.getInstance(ApiRetrofit.getInstance()) }

    private val itemCategoryOnClickListener = object : OnItemClickListener<Category> {
        override fun onClick(view: View, item: Category) {
            checkedCat.code = item.id
            _itemsCategory.value = _itemsCategory.value!!.toMutableList()
            refreshProjectList()
        }
    }

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

    fun getProjectCategories() {
        launchSerialResult({
            repository.getProjectCategories()
        },{
            if (it.isEmpty()) RESULT.EMPTY.code
            else {
                checkedCat.code = it[0].id
                _itemsCategory.value = it
                RESULT.SUCCESS.code
            }
        }, {
            repository.getProjectListByCid(0, checkedCat.code)
        }, {
            if (it.datas.isEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                _items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        })
    }

    fun refreshProjectList(isNotify: Boolean = false) {
        launchOnlyResult({
            repository.getProjectListByCid(0, checkedCat.code)
        }, {
            if (it.datas.isEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                _items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }

    fun loadMoreProjectList() {
        launchOnlyResult({
            repository.getProjectListByCid(page, checkedCat.code)
        }, {
            page = it.curPage
            val list = _items.value ?: mutableListOf<Article>()
            list.addAll(it.datas)
            if (it.offset >= it.total) RESULT.END.code else RESULT.SUCCESS.code
        }, isNotify = false)
    }
}