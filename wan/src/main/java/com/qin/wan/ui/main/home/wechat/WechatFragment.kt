package com.qin.wan.ui.main.home.wechat

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.qin.mvvm.base.BaseStateFragment
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.databinding.FragmentWechatBinding
import kotlinx.android.synthetic.main.fragment_latest.*

class WechatFragment : BaseStateFragment<WechatViewModel, FragmentWechatBinding>() {

    companion object {
        fun newInstance() = WechatFragment()
    }

    override fun layoutId() = R.layout.fragment_wechat
    override fun stateLayout() = stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel

        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.refreshWechatList() }
        }

        recyclerView.run {
            setPullRefreshEnabled(false)
            setLoadingMoreEnabled(true)
            setLoadingMoreProgressStyle(ProgressStyle.Pacman)
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onRefresh() { }
                override fun onLoadMore() {
                    viewModel.loadMoreWechatList()
                }
            })
        }
    }

    override fun lazyLoadData() {
        viewModel.getWechatCategories()
    }

    override fun onLoadResult(code: Int) {
        when(code) {
            RESULT.END.code -> ToastUtils.showLong(RESULT.END.msg)
            else -> super.onLoadResult(code)
        }
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        if (swipeRefreshLayout.isRefreshing)
            swipeRefreshLayout.isRefreshing = false
        recyclerView.loadMoreComplete()
    }

}