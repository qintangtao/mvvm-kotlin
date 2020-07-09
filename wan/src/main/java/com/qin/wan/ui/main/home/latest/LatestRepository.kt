package com.qin.wan.ui.main.home.latest

import com.qin.mvvm.base.BaseModel
import com.qin.wan.model.api.ApiRetrofit

class LatestRepository private constructor(
    private val remote: ApiRetrofit
// private val local: ApiRoom
) : BaseModel() {

    suspend fun getProjectList(page: Int) = remote.service.getProjectList(page)

    companion object {

        @Volatile
        private var INSTANCE: LatestRepository? = null

        fun getInstance(remote: ApiRetrofit) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: LatestRepository(remote).also { INSTANCE = it }
            }
    }
}