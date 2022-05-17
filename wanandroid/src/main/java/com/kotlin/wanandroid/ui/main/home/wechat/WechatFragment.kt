package com.kotlin.wanandroid.ui.main.home.wechat

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.kotlin.mvvm.base.BaseStateFragment
import com.kotlin.mvvm.network.RESULT
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.databinding.FragmentWechatBinding

class WechatFragment : BaseStateFragment<WechatViewModel, FragmentWechatBinding>() {

    companion object {
        fun newInstance() = WechatFragment()
    }

    override fun layoutId() = R.layout.fragment_wechat
    override fun stateLayout() = mBinding!!.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel

        mBinding?.run {
            swipeRefreshLayout.run {
                setColorSchemeResources(R.color.textColorPrimary)
                setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
                setOnRefreshListener { viewModel?.refreshWechatList() }
            }

            recyclerView.run {
                setPullRefreshEnabled(false)
                setLoadingMoreEnabled(true)
                setLoadingMoreProgressStyle(ProgressStyle.Pacman)
                setLoadingListener(object : XRecyclerView.LoadingListener {
                    override fun onRefresh() { }
                    override fun onLoadMore() {
                        viewModel?.loadMoreWechatList()
                    }
                })
            }
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
        mBinding?.run {
            if (swipeRefreshLayout.isRefreshing)
                swipeRefreshLayout.isRefreshing = false
            recyclerView.loadMoreComplete()
        }
    }

}