package com.kotlin.wanandroid.ui.main.discovery

import com.kotlin.mvvm.base.BaseModel
import com.kotlin.wanandroid.model.api.ApiRetrofit

class DiscoveryRepository private constructor(
    private val remote: ApiRetrofit
// private val local: ApiRoom
) : BaseModel() {

    suspend fun getBanners() = remote.service.getBanners()
    suspend fun getHotWords() = remote.service.getHotWords()
    suspend fun getFrequentlyWebsites() = remote.service.getFrequentlyWebsites()


    companion object {

        @Volatile
        private var INSTANCE: DiscoveryRepository? = null

        fun getInstance(remote: ApiRetrofit) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DiscoveryRepository(remote).also { INSTANCE = it }
            }
    }
}