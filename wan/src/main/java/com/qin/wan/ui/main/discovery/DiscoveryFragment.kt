package com.qin.wan.ui.main.discovery

import android.os.Bundle
import com.qin.mvvm.base.BaseStateFragment
import com.qin.wan.R
import com.qin.wan.databinding.FragmentDiscoveryBinding
import com.qin.wan.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_discovery.*

class DiscoveryFragment : BaseStateFragment<DiscoveryViewModel, FragmentDiscoveryBinding>() {

    companion object {
        fun newInstance() = DiscoveryFragment()
    }

    override fun layoutId() = R.layout.fragment_discovery
    override fun stateLayout() = stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel

        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.getData() }
        }

        nestedScollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (activity is MainActivity && scrollY != oldScrollY) {
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
        }
    }

    override fun lazyLoadData() {
        viewModel.getData(true)
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        if (swipeRefreshLayout.isRefreshing)
            swipeRefreshLayout.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        bannerView.startAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        bannerView.stopAutoPlay()
    }
}