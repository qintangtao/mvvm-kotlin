package com.qin.wan.ui.main.system

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.base.BaseViewModel
import com.qin.mvvm.network.RESULT
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.model.bean.Category

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