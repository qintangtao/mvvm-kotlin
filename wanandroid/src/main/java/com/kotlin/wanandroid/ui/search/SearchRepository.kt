package com.kotlin.wanandroid.ui.search

import com.kotlin.mvvm.base.BaseModel
import com.kotlin.wanandroid.model.api.ApiRetrofit
import com.kotlin.wanandroid.model.store.SearchHistoryStore

class SearchRepository private constructor(
    private val remote: ApiRetrofit
// private val local: ApiRoom
) : BaseModel() {

    suspend fun search(keywords: String, page: Int) = remote.service.search(keywords, page)

    suspend fun getHotSearch() = remote.service.getHotWords()

    fun saveSearchHistory(searchWords: String) {
        SearchHistoryStore.saveSearchHistory(searchWords)
    }

    fun deleteSearchHistory(searchWords: String) {
        SearchHistoryStore.deleteSearchHistory(searchWords)
    }

    fun getSearchHisory() = SearchHistoryStore.getSearchHistory()

    companion object {

        @Volatile
        private var INSTANCE: SearchRepository? = null

        fun getInstance(remote: ApiRetrofit) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SearchRepository(remote).also { INSTANCE = it }
            }

        const val SELECTED_KEYWORDS = "selected_keywords"
        //const val SEARCH_KEYWORDS = "search_keywords"
    }
}