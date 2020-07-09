package com.qin.wan.ui.main.dashboard

import android.os.Bundle
import android.util.Log
import com.qin.mvvm.base.BaseStateFragment
import com.qin.mvvm.event.Message
import com.qin.wan.R
import com.qin.wan.databinding.FragmentDashboardBinding
import kotlinx.android.synthetic.main.fragment_dashboard.stateLayout
import kotlinx.android.synthetic.main.fragment_dashboard.swipeRefreshLayout

class DashboardFragment : BaseStateFragment<DashboardViewModel, FragmentDashboardBinding>() {

    private val TAG = "StatusLayout"

    companion object {
        fun newInstance() = DashboardFragment()
    }

    override fun layoutId() = R.layout.fragment_dashboard
    override fun stateLayout() = stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel

        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.testStatus() }
        }
    }

    override fun lazyLoadData() {
        Log.d(TAG, "lazyLoadData")
        viewModel.testStatus(true)
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        if (swipeRefreshLayout.isRefreshing)
            swipeRefreshLayout.isRefreshing = false
    }
}