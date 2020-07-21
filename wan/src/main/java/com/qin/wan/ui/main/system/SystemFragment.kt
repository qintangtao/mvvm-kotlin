package com.qin.wan.ui.main.system

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.qin.mvvm.adapter.SimpleFragmentPagerAdapter
import com.qin.mvvm.base.BaseStateFragment
import com.qin.wan.R
import com.qin.wan.databinding.FragmentSystemBinding
import com.qin.wan.ui.main.MainActivity
import com.qin.wan.ui.main.system.category.SystemCategoryFragment
import com.qin.wan.ui.main.system.pager.SystemPagerFragment
import kotlinx.android.synthetic.main.fragment_system.*

class SystemFragment : BaseStateFragment<SystemViewModel, FragmentSystemBinding>() {

    companion object {
        fun newInstance() = SystemFragment()
    }

    private var categoryFragment: SystemCategoryFragment? = null
    private val titles = mutableListOf<String>()
    private var fragments = mutableListOf<SystemPagerFragment>()
    private var currentOffset = 0

    override fun layoutId() = R.layout.fragment_system
    override fun stateLayout() = stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mBinding?.viewModel = viewModel

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (activity is MainActivity && this.currentOffset != offset) {
                (activity as MainActivity).animateBottomNavigationView(offset > currentOffset)
                currentOffset = offset
            }
        })

        ivFilter.setOnClickListener {
            categoryFragment?.show(childFragmentManager)
        }

        viewModel.itemsCategory.observe(viewLifecycleOwner, Observer {
            titles.clear()
            fragments.clear()
            it.forEach {
                titles.add(it.name)
                fragments.add(SystemPagerFragment.newInstance(it))
            }

            viewPager.adapter = SimpleFragmentPagerAdapter(childFragmentManager, fragments, titles)
            //太多了会造成卡顿，通过viewmodel的生命周期，在初始化加载数据的时候，不重新加载新的数据，直接使用旧数据
            //viewPager.offscreenPageLimit = fragments.size
            tabLayout.setupWithViewPager(viewPager)

            categoryFragment = SystemCategoryFragment.newInstance(ArrayList(it))
        })
    }

    override fun lazyLoadData() {
        viewModel.getArticleCategories();
    }

    override fun onLoadStart() {
        viewPager.visibility = View.GONE
        super.onLoadStart()
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        if (!isErrorLoaded && !isEmptyLoaded) {
            tvState.visibility = View.GONE
            viewPager.visibility = View.VISIBLE
        }
    }
}