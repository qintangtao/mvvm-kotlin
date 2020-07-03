package com.qin.wan.ui.main.notifications

import android.os.Bundle
import android.util.Log
import com.qin.mvvm.base.BaseStateFragment
import com.qin.wan.R
import kotlinx.android.synthetic.main.fragment_notifications.*
import com.qin.wan.databinding.FragmentNotificationsBinding

class NotificationsFragment : BaseStateFragment<NotificationsViewModel, FragmentNotificationsBinding>() {

    private val TAG = "StatusLayout"

    companion object {
        fun newInstance() = NotificationsFragment()
    }

    override fun layoutId() = R.layout.fragment_notifications
    override fun stateLayout() = stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mBinding?.viewModel = viewModel
    }

    override fun lazyLoadData() {
        Log.d(TAG, "lazyLoadData")
        viewModel.testStatus();
        /*
        viewModel.run {
            testStatus().observe(this@NotificationsFragment, Observer {
                if (TextUtils.isEmpty(it)) {
                    isEmpty = true
                } else {
                    text_notifications.text = it
                }
                Log.d(TAG, it)
            })
        }*/
    }

}