package com.qin.wan.ui.common

import com.qin.mvvm.base.BaseModel
import com.qin.wan.model.api.ApiRetrofit

class CommonRepository private constructor(
    private val remote: ApiRetrofit
// private val local: ApiRoom
) : BaseModel() {

    suspend fun login(username: String, password: String) =  remote.service.login(username, password)

        companion object {

        @Volatile
        private var INSTANCE: CommonRepository? = null

        fun getInstance(remote: ApiRetrofit) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: CommonRepository(remote).also { INSTANCE = it }
            }
    }
}