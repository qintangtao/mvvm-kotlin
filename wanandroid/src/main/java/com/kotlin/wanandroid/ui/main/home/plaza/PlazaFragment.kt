package com.kotlin.wanandroid.ui.main.home.plaza

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.kotlin.mvvm.base.BaseStateFragment
import com.kotlin.mvvm.network.RESULT
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.databinding.FragmentPlazaBinding

class PlazaFragment : BaseStateFragment<PlazaViewModel, FragmentPlazaBinding>() {

    companion object {
        fun newInstance() = PlazaFragment()
    }

    override fun stateLayout() = mBinding.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel

        mBinding.run {
            swipeRefreshLayout.run {
                setColorSchemeResources(R.color.textColorPrimary)
                setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
                setOnRefreshListener { viewModel?.refreshUserArticleList() }
            }

            recyclerView.run {
                setPullRefreshEnabled(false)
                setLoadingMoreEnabled(true)
                setLoadingMoreProgressStyle(ProgressStyle.Pacman)
                setLoadingListener(object : XRecyclerView.LoadingListener {
                    override fun onRefresh() {
                    }

                    override fun onLoadMore() {
                        viewModel?.loadMoreUserArticleList()
                    }
                })
            }
        }

    }
    override fun lazyLoadData() {
        viewModel.refreshUserArticleList(true)
    }

    override fun onLoadResult(code: Int) {
        when(code) {
            RESULT.END.code -> ToastUtils.showLong(RESULT.END.msg)
            else -> super.onLoadResult(code)
        }
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        mBinding.run {
            if (swipeRefreshLayout.isRefreshing)
                swipeRefreshLayout.isRefreshing = false
            recyclerView.loadMoreComplete()
        }
    }
}