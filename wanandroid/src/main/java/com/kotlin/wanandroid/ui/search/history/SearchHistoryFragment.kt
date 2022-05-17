package com.kotlin.wanandroid.ui.search.history

import android.os.Bundle
import com.kotlin.mvvm.base.BaseStateFragment
import com.kotlin.mvvm.bus.Bus
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.databinding.FragmentSearchHistoryBinding
import com.kotlin.wanandroid.ui.search.SearchActivity
import com.kotlin.wanandroid.ui.search.SearchRepository

class SearchHistoryFragment : BaseStateFragment<SearchHistoryViewModel, FragmentSearchHistoryBinding>() {

    companion object {
        fun newInstance() = SearchHistoryFragment()
    }

    override fun layoutId() = R.layout.fragment_search_history
    override fun stateLayout() = mBinding!!.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel

        Bus.observe<String>(SearchRepository.SELECTED_KEYWORDS, this) {
            (activity as? SearchActivity)?.fillSearchInput(it)
        }
    }

    override fun lazyLoadData() {
        viewModel.initData()
    }

    fun addSearchHistory(keywords: String) {
        viewModel.addSearchHistory(keywords)
    }
}