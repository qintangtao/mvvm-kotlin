package com.kotlin.wanandroid.ui.main.system.pager

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.kotlin.mvvm.base.BaseStateFragment
import com.kotlin.mvvm.network.RESULT
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.databinding.FragmentSystemPagerBinding
import com.kotlin.wanandroid.model.bean.Category

class SystemPagerFragment : BaseStateFragment<SystemPagerViewModel, FragmentSystemPagerBinding>() {

    companion object {
        private const val PARAM_CATEGORY = "param_category"
        fun newInstance(category: Category) : SystemPagerFragment {
            return SystemPagerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_CATEGORY, category)
                }
            }
        }
    }

    private lateinit var category: Category

    override fun layoutId() = R.layout.fragment_system_pager
    override fun stateLayout() = mBinding!!.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel

        category = arguments?.getParcelable(PARAM_CATEGORY)!!

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
                setLoadingListener(object : XRecyclerView.LoadingListener {
                    override fun onRefresh() { }
                    override fun onLoadMore() {
                        viewModel?.loadMoreArticleList()
                    }
                })
            }
        }

    }

    override fun lazyLoadData() {
        viewModel.getArticleList(category.children)
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

    fun getCheckedPosition() =  category.id to  viewModel.checkedCat.code

    fun check(position: Int) {
        val position2 = viewModel.itemsCategory.value?.get(position)?.id ?: return
        viewModel.check(position2)
    }
}