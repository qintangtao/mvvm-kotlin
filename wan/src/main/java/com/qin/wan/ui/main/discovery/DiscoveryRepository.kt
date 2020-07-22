package com.qin.wan.ui.main.discovery

import com.qin.mvvm.base.BaseModel
import com.qin.wan.model.api.ApiRetrofit

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