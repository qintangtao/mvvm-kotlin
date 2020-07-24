package com.qin.wan.ui.main.home.project

import android.content.Intent
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
import com.qin.wan.ui.main.home.HomeRepository
import com.qin.wan.ui.common.OnItemClickListener
import com.qin.wan.ui.detail.DetailActivity
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ProjectViewModel : BaseViewModel() {
    private val repository by lazy { HomeRepository.getInstance(ApiRetrofit.getInstance()) }

    private val itemCategoryOnClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(view: View, item: Category) {
            checkedCat.code = item.id
            val list = itemsCategory.value
            list?.let {
                itemsCategory.value = mutableListOf<Category>().apply {
                    addAll(it)
                }
            }
            refreshProjectList()
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
            val list = items.value
            val item2 = list?.find { it.id == item.id } ?: return
            item2.collect = !item2.collect
            items.value = mutableListOf<Article>().apply {
                addAll(list)
            }
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

    fun getProjectCategories() {
        launchSerialResult({
            repository.getProjectCategories()
        },{
            if (it.isEmpty()) RESULT.EMPTY.code
            else {
                checkedCat.code = it[0].id
                itemsCategory.value = it
                RESULT.SUCCESS.code
            }
        }, {
            repository.getProjectListByCid(0, checkedCat.code)
        }, {
            if (it.datas.isEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                items.value = it.datas.toMutableList()
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
                items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }

    fun loadMoreProjectList() {
        launchOnlyResult({
            repository.getProjectListByCid(page, checkedCat.code)
        }, {
            page = it.curPage
            val list = items.value ?: mutableListOf<Article>()
            list.addAll(it.datas)
            if (it.offset >= it.total) RESULT.END.code else RESULT.SUCCESS.code
        }, isNotify = false)
    }
}