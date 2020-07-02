package com.qin.wan.ui.main.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.qin.wan.R
import kotlinx.android.synthetic.main.fragment_home.*
import androidx.lifecycle.Observer
import com.qin.mvvm.base.BaseStateFragment

class HomeFragment : BaseStateFragment<HomeViewModel, ViewDataBinding>() {

    private val TAG = "StatusLayout"

    override fun layoutId() = R.layout.fragment_home
    override fun stateLayout() = stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        stateLayout.run {
            setEmptyClickListener{
                lazyLoadData()
            }
            setErrorClickListener{
                lazyLoadData()
            }
        }
    }

    override fun lazyLoadData() {
        Log.d(TAG, "lazyLoadData")
        viewModel.run {
            testStatus().observe(this@HomeFragment, Observer {
                if (TextUtils.isEmpty(it)) {
                    isEmpty = true
                } else {
                    text_home.text = it
                }
                Log.d(TAG, it)
            })
        }
    }
}