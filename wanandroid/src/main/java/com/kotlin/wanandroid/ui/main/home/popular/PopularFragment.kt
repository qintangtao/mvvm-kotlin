package com.kotlin.wanandroid.ui.main.home.popular

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView.LoadingListener
import com.kotlin.mvvm.base.BaseStateFragment
import com.kotlin.mvvm.event.Message
import com.kotlin.mvvm.network.RESULT
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.databinding.FragmentPopularBinding
import com.kotlin.wanandroid.model.bean.Article
import com.kotlin.wanandroid.ui.detail.DetailActivity

class PopularFragment : BaseStateFragment<PopularViewModel, FragmentPopularBinding>() {

    companion object {
        fun newInstance() = PopularFragment()
        const val START_DETAIL_ARTICLE = 10000
    }

    override fun layoutId() = R.layout.fragment_popular
    override fun stateLayout() = mBinding!!.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel

        mBinding?.run {
            swipeRefreshLayout.run {
                setColorSchemeResources(R.color.textColorPrimary)
                setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
                setOnRefreshListener { viewModel?.refreshArticleList() }
            }

            recyclerView.run {
                setPullRefreshEnabled(false)
                setLoadingMoreEnabled(true)
                setLoadingMoreProgressStyle(ProgressStyle.Pacman)
                setLoadingListener(object : LoadingListener {
                    override fun onRefresh() {
                    }
                    override fun onLoadMore() {
                        viewModel?.loadMoreArticleList()
                    }
                })
            }
        }

    }

    override fun lazyLoadData() {
        viewModel?.refreshArticleList(true)
    }

    override fun onLoadEvent(msg: Message) {
        when(msg.code) {
            START_DETAIL_ARTICLE -> {
                startActivity(Intent().apply {
                    setClass(requireActivity(), DetailActivity::class.java)
                    putExtra(DetailActivity.PARAM_ARTICLE, msg.obj as Article)
                })
            }
            else -> {
                super.onLoadEvent(msg)
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
}