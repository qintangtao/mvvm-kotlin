package com.qin.wan.ui.main.system

import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.base.BaseViewModel
import com.qin.mvvm.network.RESULT
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.model.bean.Category

class SystemViewModel : BaseViewModel() {
    private val repository by lazy { SystemRepository.getInstance(ApiRetrofit.getInstance()) }

    val itemsCategory = MutableLiveData<MutableList<Category>>()

    fun getArticleCategories() {
        launchOnlyResult({
            repository.getArticleCategories()
        }, {
            if (it.isEmpty()) RESULT.EMPTY.code
            else {
                itemsCategory.value = it
                RESULT.SUCCESS.code
            }
        })
    }
}