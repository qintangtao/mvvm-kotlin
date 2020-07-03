package com.qin.wan.ui.main.dashboard

import android.text.TextUtils
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.qin.mvvm.base.BaseStateFragment
import com.qin.wan.R
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseStateFragment<DashboardViewModel, ViewDataBinding>() {

    private val TAG = "StatusLayout"

    companion object {
        fun newInstance() = DashboardFragment()
    }

    override fun layoutId() = R.layout.fragment_dashboard
    override fun stateLayout() = stateLayout

    override fun lazyLoadData() {
        Log.d(TAG, "lazyLoadData")
        viewModel.run {
            testStatus().observe(this@DashboardFragment, Observer {
                if (TextUtils.isEmpty(it)) {
                    isEmpty = true
                } else {
                    text_dashboard.text = it
                }
                Log.d(TAG, it)
            })
        }
    }

}