package com.qin.wan.ui.main.navigation

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.qin.mvvm.base.BaseStateFragment
import com.qin.wan.R
import com.qin.wan.databinding.FragmentNavigationBinding
import com.qin.wan.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_discovery.stateLayout
import kotlinx.android.synthetic.main.fragment_discovery.swipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_navigation.*

class NavigationFragment : BaseStateFragment<NavigationViewModel, FragmentNavigationBinding>() {

    companion object {
        fun newInstance() = NavigationFragment()
    }

    private var currentPosition = 0

    override fun layoutId() = R.layout.fragment_navigation
    override fun stateLayout() = stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel

        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.getNavigation(false)  }
        }

        recyclerView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (activity is MainActivity && scrollY != oldScrollY) {
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
            if (scrollY < oldScrollY) {
                tvFloatTitle.text = viewModel.items.value?.get(currentPosition)?.name
            }
            val lm = recyclerView.layoutManager as LinearLayoutManager
            val nextView = lm.findViewByPosition(currentPosition + 1)
            if (nextView != null) {
                tvFloatTitle.y = if (nextView.top < tvFloatTitle.measuredHeight) {
                    (nextView.top - tvFloatTitle.measuredHeight).toFloat()
                } else {
                    0f
                }
            }
            currentPosition = lm.findFirstVisibleItemPosition()
            if (scrollY > oldScrollY) {
                tvFloatTitle.text = viewModel.items.value?.get(currentPosition)?.name
            }
        }
    }

    override fun lazyLoadData() {
        viewModel.getNavigation(true)
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        if (swipeRefreshLayout.isRefreshing)
            swipeRefreshLayout.isRefreshing = false
    }
}