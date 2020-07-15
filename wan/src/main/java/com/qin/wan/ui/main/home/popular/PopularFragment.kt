package com.qin.wan.ui.main.home.popular

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView.LoadingListener
import com.qin.mvvm.base.BaseStateFragment
import com.qin.mvvm.event.Message
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.databinding.FragmentPopularBinding
import com.qin.wan.model.bean.Article
import com.qin.wan.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_popular.*

class PopularFragment : BaseStateFragment<PopularViewModel, FragmentPopularBinding>() {

    companion object {
        fun newInstance() = PopularFragment()
        const val START_DETAIL_ARTICLE = 10000
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

    override fun onLoadEvent(msg: Message) {
        when(msg.code) {
            START_DETAIL_ARTICLE -> {
                startActivity(Intent().apply {
                    setClass(activity!!, DetailActivity::class.java)
                    putExtra(DetailActivity.PARAM_ARTICLE, msg.obj as Article)
                })
            }
            else -> {
                super.onLoadEvent(msg)
            }
        }
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