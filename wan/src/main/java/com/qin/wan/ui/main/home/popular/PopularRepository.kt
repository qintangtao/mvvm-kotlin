package com.qin.wan.ui.main.home.popular

import com.qin.mvvm.base.BaseModel
import com.qin.wan.model.api.ApiRetrofit

class PopularRepository private constructor(
    private val remote: ApiRetrofit
// private val local: ApiRoom
) : BaseModel() {

    suspend fun getTopArticleList() = remote.service.getTopArticleList()
    suspend fun getArticleList(page: Int) = remote.service.getArticleList(page)

    companion object {

        @Volatile
        private var INSTANCE: PopularRepository? = null

        fun getInstance(remote: ApiRetrofit) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PopularRepository(remote).also { INSTANCE = it }
            }
    }
}