package com.qin.wan.ui.main.home.plaza

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.qin.mvvm.base.BaseStateFragment
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.databinding.FragmentPlazaBinding
import kotlinx.android.synthetic.main.fragment_latest.*

class PlazaFragment : BaseStateFragment<PlazaViewModel, FragmentPlazaBinding>() {

    companion object {
        fun newInstance() = PlazaFragment()
    }

    override fun layoutId() = R.layout.fragment_plaza
    override fun stateLayout() = stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel

        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.refreshUserArticleList() }
        }

        recyclerView.run {
            setPullRefreshEnabled(false)
            setLoadingMoreEnabled(true)
            setLoadingMoreProgressStyle(ProgressStyle.Pacman)
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onRefresh() {
                }

                override fun onLoadMore() {
                    viewModel.loadMoreUserArticleList()
                }
            })
        }
    }
    override fun lazyLoadData() {
        viewModel.refreshUserArticleList(true)
    }

    override fun onLoadResult(code: Int) {
        super.onLoadResult(code)
        if (code == RESULT.END.code) ToastUtils.showLong(RESULT.END.msg)
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        if (swipeRefreshLayout.isRefreshing)
            swipeRefreshLayout.isRefreshing = false
        recyclerView.loadMoreComplete()
    }
}