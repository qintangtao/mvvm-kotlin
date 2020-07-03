package com.qin.wan.ui.main.home

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.qin.mvvm.adapter.SimpleFragmentPagerAdapter
import com.qin.wan.R
import com.qin.mvvm.base.BaseFragment
import com.qin.mvvm.base.NoViewModel
import com.qin.wan.ui.main.MainActivity
import com.qin.wan.ui.main.dashboard.DashboardFragment
import com.qin.wan.ui.main.notifications.NotificationsFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<NoViewModel, ViewDataBinding>() {

    private lateinit var fragments: List<Fragment>
    private var currentOffset = 0

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun layoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        fragments = listOf(
            DashboardFragment.newInstance(),
            NotificationsFragment.newInstance()
        )

        val titles = listOf<CharSequence>(
            getString(R.string.popular_articles),
            getString(R.string.latest_project)
        )

        viewPager.adapter = SimpleFragmentPagerAdapter(childFragmentManager, fragments, titles)
        viewPager.offscreenPageLimit = fragments.size
        tabLayout.setupWithViewPager(viewPager)

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (activity is MainActivity && this.currentOffset != offset) {
                (activity as MainActivity).animateBottomNavigationView(offset > currentOffset)
                currentOffset = offset
            }
        })
        llSearch.setOnClickListener {   }

    }

}