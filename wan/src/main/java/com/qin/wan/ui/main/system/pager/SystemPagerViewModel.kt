package com.qin.wan.ui.main.system.pager

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
import com.qin.wan.ui.main.system.SystemRepository
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SystemPagerViewModel : BaseViewModel() {

    private val repository by lazy { SystemRepository.getInstance(ApiRetrofit.getInstance()) }

    private val itemCategoryOnClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(view: View, item: Category) {
            checkedCat.code = item.id
            _itemsCategory.value = itemsCategory.value!!.toMutableList()
            refreshArticleList(true)
        }
    }

    private val itemOnClickListener = object : OnItemClickListener<Article> {
        override fun onItemClick(view: View, item: Article) {
            when(view.id) {
                R.id.iv_collect -> {
                    item.collect = !item.collect
                    _items.value = items.value!!.toMutableList()
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

    private val _itemsCategory = MutableLiveData<MutableList<Category>>()
    private val _items = MutableLiveData<MutableList<Article>>()

    val checkedCat = Message()

    val itemsCategory: LiveData<MutableList<Category>> = _itemsCategory
    val itemBindingCategory = ItemBinding.of<Category>(BR.itemBean, R.layout.item_category_sub)
        .bindExtra(BR.listenner, itemCategoryOnClickListener)
        .bindExtra(BR.checkedCat, checkedCat)

    val items : LiveData<MutableList<Article>> = _items
    val itemBinding = ItemBinding.of<Article>(BR.itemBean, R.layout.item_article)
        .bindExtra(BR.listenner, itemOnClickListener)


    fun getArticleList(categorys: ArrayList<Category>) {
        if(!_itemsCategory.value.isNullOrEmpty() && !_items.value.isNullOrEmpty()) {
            //重新设置，触发界面更新数据
            _itemsCategory.value = _itemsCategory.value!!.toMutableList()
            _items.value = _items.value!!.toMutableList()
            callComplete()
            return
        }

        if (categorys.isNullOrEmpty()) {
            callResult(RESULT.EMPTY.code)
            return
        }

        //default selected 0 index.
        checkedCat.code = categorys[0].id
        _itemsCategory.value = categorys.toMutableList()

        refreshArticleList(true)
    }

    fun refreshArticleList(isNotify: Boolean = false) {
        launchOnlyResult({
            repository.getArticleListByCid(0, checkedCat.code)
        }, {
            if (it.datas.isNullOrEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                _items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }

    fun loadMoreArticleList() {
        launchOnlyResult({
            repository.getArticleListByCid(page, checkedCat.code)
        }, {
            page = it.curPage
            if (!it.datas.isNullOrEmpty()) {
                val list = _items.value ?: mutableListOf<Article>()
                list.addAll(it.datas)
            }
            if (it.offset >= it.total) RESULT.END.code else RESULT.SUCCESS.code
        }, isNotify = false)
    }

    fun check(id: Int) {
        if (id != checkedCat.code) {
            checkedCat.code = id
            _itemsCategory.value = _itemsCategory.value!!.toMutableList()
            refreshArticleList(true)
        }
    }
}