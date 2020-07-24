package com.qin.wan.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.base.BaseViewModel
import com.qin.wan.model.bean.Article

class DetailViewModel : BaseViewModel() {

    private val _article = MutableLiveData<Article>()
    private val _title = MutableLiveData<String>()

    val article: LiveData<Article> = _article
    val title: LiveData<String> = _title

    fun setArtivle(article: Article) {
        _article.value = article
    }

    fun setTitle(title: String) {
        _title.value = title
    }
}