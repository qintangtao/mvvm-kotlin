package com.kotlin.wanandroid.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.kotlin.mvvm.adapter.SimpleFragmentPagerAdapter
import com.kotlin.wanandroid.R
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.base.NoViewModel
import com.kotlin.wanandroid.databinding.FragmentHomeBinding
import com.kotlin.wanandroid.ui.main.MainActivity
import com.kotlin.wanandroid.ui.main.home.latest.LatestFragment
import com.kotlin.wanandroid.ui.main.home.plaza.PlazaFragment
import com.kotlin.wanandroid.ui.main.home.popular.PopularFragment
import com.kotlin.wanandroid.ui.main.home.project.ProjectFragment
import com.kotlin.wanandroid.ui.main.home.wechat.WechatFragment
import com.kotlin.wanandroid.ui.search.SearchActivity

class HomeFragment : BaseFragment<NoViewModel, FragmentHomeBinding>() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var fragments: List<Fragment>
    private var currentOffset = 0


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        Log.d("BaseFragment", "HomeFragment=============")

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

        Log.d("BaseFragment", mBinding.toString())

        mBinding.run {
            viewPager.adapter = SimpleFragmentPagerAdapter(childFragmentManager, fragments, titles)
            viewPager.offscreenPageLimit = fragments.size
            tabLayout.setupWithViewPager(viewPager)
        }

        mBinding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (activity is MainActivity && this.currentOffset != offset) {
                (activity as MainActivity).animateBottomNavigationView(offset > currentOffset)
                currentOffset = offset
            }
        })
        mBinding.llSearch.setOnClickListener { startActivity(Intent(this.context, SearchActivity::class.java))  }
    }

}