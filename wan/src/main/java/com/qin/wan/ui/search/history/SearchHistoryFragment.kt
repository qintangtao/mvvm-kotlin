package com.qin.wan.ui.search.history

import android.os.Bundle
import com.qin.mvvm.base.BaseStateFragment
import com.qin.mvvm.bus.Bus
import com.qin.wan.R
import com.qin.wan.databinding.FragmentSearchHistoryBinding
import com.qin.wan.ui.search.SearchActivity
import com.qin.wan.ui.search.SearchRepository
import kotlinx.android.synthetic.main.fragment_search_history.*

class SearchHistoryFragment : BaseStateFragment<SearchHistoryViewModel, FragmentSearchHistoryBinding>() {

    companion object {
        fun newInstance() = SearchHistoryFragment()
    }

    override fun layoutId() = R.layout.fragment_search_history
    override fun stateLayout() = stateLayout

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