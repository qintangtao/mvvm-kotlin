package com.qin.mvvm.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.tabs.TabLayout

object TabLayoutAdapter {

    @JvmStatic
    @BindingAdapter(value = ["items"], requireAll = false)
    fun setTabText(tabLayout: TabLayout, items: List<String>?) {
        items?.forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it))
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["tabItemClick"], requireAll = false)
    fun tabItemClick(tabLayout: TabLayout, listener: TabLayout.BaseOnTabSelectedListener<TabLayout.Tab>) {
        tabLayout.addOnTabSelectedListener(listener)
    }


}