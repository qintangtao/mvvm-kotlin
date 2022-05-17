package com.kotlin.wanandroid.ui.main.system

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.network.RESULT
import com.kotlin.wanandroid.ui.main.system.SystemRepository
import com.kotlin.wanandroid.model.api.ApiRetrofit
import com.kotlin.wanandroid.model.bean.Category

class SystemViewModel : BaseViewModel() {
    private val repository by lazy { SystemRepository.getInstance(ApiRetrofit.getInstance()) }

    private val _itemsCategory = MutableLiveData<MutableList<Category>>()

    val itemsCategory : LiveData<MutableList<Category>> = _itemsCategory

    fun getArticleCategories() {
        launchOnlyResult({
            repository.getArticleCategories()
        }, {
            if (it.isEmpty()) RESULT.EMPTY.code
            else {
                _itemsCategory.value = it
                RESULT.SUCCESS.code
            }
        })
    }
}