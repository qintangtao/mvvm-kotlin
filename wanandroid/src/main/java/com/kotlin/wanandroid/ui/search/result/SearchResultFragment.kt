package com.kotlin.wanandroid.ui.search.result

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.kotlin.mvvm.base.BaseStateFragment
import com.kotlin.mvvm.network.RESULT
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.databinding.FragmentSearchResultBinding


class SearchResultFragment : BaseStateFragment<SearchResultViewModel, FragmentSearchResultBinding>() {

    companion object {
        fun newInstance() = SearchResultFragment()
    }

    override fun layoutId() = R.layout.fragment_search_result
    override fun stateLayout() = mBinding!!.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel

        mBinding?.run {
            swipeRefreshLayout.run {
                setColorSchemeResources(R.color.textColorPrimary)
                setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
                setOnRefreshListener { viewModel?.search() }
            }

            recyclerView.run {
                setPullRefreshEnabled(false)
                setLoadingMoreEnabled(true)
                setLoadingMoreProgressStyle(ProgressStyle.Pacman)
                setLoadingListener(object : XRecyclerView.LoadingListener {
                    override fun onRefresh() {
                    }

                    override fun onLoadMore() {
                        viewModel?.loadMore()
                    }
                })
            }
        }

    }

    override fun onLoadResult(code: Int) {
        when(code) {
            RESULT.END.code -> ToastUtils.showLong(RESULT.END.msg)
            else -> super.onLoadResult(code)
        }
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        mBinding?.run {
            if (swipeRefreshLayout.isRefreshing)
                swipeRefreshLayout.isRefreshing = false
            recyclerView.loadMoreComplete()
        }
    }

    fun search(keywords: String) {
        viewModel.search(keywords, true)
    }
}