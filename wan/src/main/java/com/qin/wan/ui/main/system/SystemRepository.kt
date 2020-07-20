package com.qin.wan.ui.main.system

import com.qin.mvvm.base.BaseModel
import com.qin.wan.model.api.ApiRetrofit

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