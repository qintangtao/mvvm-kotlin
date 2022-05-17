package com.kotlin.wanandroid.ui.main.system

import com.kotlin.mvvm.base.BaseModel
import com.kotlin.wanandroid.model.api.ApiRetrofit

class SystemRepository private constructor(
    private val remote: ApiRetrofit
// private val local: ApiRoom
) : BaseModel() {

    suspend fun getArticleCategories() = remote.service.getArticleCategories()
    suspend fun getArticleListByCid(page: Int, cid: Int) = remote.service.getArticleListByCid(page, cid)


    companion object {

        @Volatile
        private var INSTANCE: SystemRepository? = null

        fun getInstance(remote: ApiRetrofit) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SystemRepository(remote).also { INSTANCE = it }
            }
    }
}