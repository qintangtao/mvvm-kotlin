package com.qin.wan.ui.detail

import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.base.BaseViewModel
import com.qin.wan.model.bean.Article

class DetailViewModel : BaseViewModel() {

    val article = MutableLiveData<Article>()
    val title = MutableLiveData<String>()


}