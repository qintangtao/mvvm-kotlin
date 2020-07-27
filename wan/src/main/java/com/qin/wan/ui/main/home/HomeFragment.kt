package com.qin.wan.ui.main.home

import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.qin.mvvm.adapter.SimpleFragmentPagerAdapter
import com.qin.wan.R
import com.qin.mvvm.base.BaseFragment
import com.qin.mvvm.base.NoViewModel
import com.qin.wan.ui.main.MainActivity
import com.qin.wan.ui.main.home.latest.LatestFragment
import com.qin.wan.ui.main.home.plaza.PlazaFragment
import com.qin.wan.ui.main.home.popular.PopularFragment
import com.qin.wan.ui.main.home.project.ProjectFragment
import com.qin.wan.ui.main.home.wechat.WechatFragment
import com.qin.wan.ui.search.SearchActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<NoViewModel, ViewDataBinding>() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var fragments: List<Fragment>
    private var currentOffset = 0

    override fun layoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        fragments = listOf(
            PopularFragment.newInstance(),
            LatestFragment.newInstance(),
            PlazaFragment.newInstance(),
            ProjectFragment.newInstance(),
            WechatFragment.newInstance()
        )

        val titles = listOf<CharSequence>(
            getString(R.string.popular_articles),
            getString(R.string.latest_project),
            getString(R.string.plaza),
            getString(R.string.project_category),
            getString(R.string.wechat_public)
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
        llSearch.setOnClickListener { startActivity(Intent(this.context, SearchActivity::class.java))  }
    }

}