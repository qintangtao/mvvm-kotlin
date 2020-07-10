package com.qin.wan.ui.main.home.latest

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.qin.mvvm.base.BaseStateFragment
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.databinding.FragmentLatestBinding
import kotlinx.android.synthetic.main.fragment_latest.recyclerView
import kotlinx.android.synthetic.main.fragment_latest.stateLayout
import kotlinx.android.synthetic.main.fragment_latest.swipeRefreshLayout

class LatestFragment : BaseStateFragment<LatestViewModel, FragmentLatestBinding>() {

    companion object {
        fun newInstance() = LatestFragment()
    }

    override fun layoutId() = R.layout.fragment_latest
    override fun stateLayout() = stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel

        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.refreshProjectList() }
        }

        recyclerView.run {
            setPullRefreshEnabled(false)
            setLoadingMoreEnabled(true)
            setLoadingMoreProgressStyle(ProgressStyle.Pacman)
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onRefresh() {
                }

                override fun onLoadMore() {
                    viewModel.loadMoreProjectList()
                }
            })
        }
    }
    override fun lazyLoadData() {
        viewModel.refreshProjectList(true)
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