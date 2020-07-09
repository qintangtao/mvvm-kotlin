package com.qin.wan.ui.main.home.popular

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView.LoadingListener
import com.qin.mvvm.base.BaseStateFragment
import com.qin.mvvm.event.Message
import com.qin.wan.R
import com.qin.wan.databinding.FragmentPopularBinding
import kotlinx.android.synthetic.main.fragment_dashboard.stateLayout
import kotlinx.android.synthetic.main.fragment_popular.*

class PopularFragment : BaseStateFragment<PopularViewModel, FragmentPopularBinding>() {

    companion object {
        fun newInstance() = PopularFragment()
    }

    override fun layoutId() = R.layout.fragment_popular
    override fun stateLayout() = stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel

        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.refreshArticleList() }
        }

        recyclerView.run {
            setPullRefreshEnabled(false)
            setLoadingMoreEnabled(true)
            setLoadingMoreProgressStyle(ProgressStyle.Pacman)
            setLoadingListener(object : LoadingListener {
                override fun onRefresh() {
                }
                override fun onLoadMore() {
                    viewModel.loadMoreArticleList()
                }
            })
        }
    }

    override fun lazyLoadData() {
        viewModel.refreshArticleList(true)
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        if (swipeRefreshLayout.isRefreshing)
            swipeRefreshLayout.isRefreshing = false
        recyclerView.loadMoreComplete()
    }
}