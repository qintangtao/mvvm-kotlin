package com.qin.wan.ui.main.system.pager

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.qin.mvvm.base.BaseStateFragment
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.databinding.FragmentSystemPagerBinding
import com.qin.wan.model.bean.Category
import kotlinx.android.synthetic.main.fragment_system_pager.*
import kotlinx.android.synthetic.main.fragment_system_pager.stateLayout

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
    override fun stateLayout() = stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel

        category = arguments?.getParcelable(PARAM_CATEGORY)!!

        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.refreshArticleList() }
        }

        recyclerView.run {
            setPullRefreshEnabled(false)
            setLoadingMoreEnabled(true)
            setLoadingMoreProgressStyle(ProgressStyle.Pacman)
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onRefresh() { }
                override fun onLoadMore() {
                    viewModel.loadMoreArticleList()
                }
            })
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
        if (swipeRefreshLayout.isRefreshing)
            swipeRefreshLayout.isRefreshing = false
        recyclerView.loadMoreComplete()
    }

    fun getCheckedPosition() =  category.id to  viewModel.checkedCat.code

    fun check(position: Int) {
        val position = viewModel.itemsCategory.value?.get(position)?.id ?: return
        viewModel.check(position)
    }
}