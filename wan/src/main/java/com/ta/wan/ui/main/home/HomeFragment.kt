package com.ta.wan.ui.main.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.ta.mvvm.base.BaseFragment
import com.ta.mvvm.event.Message
import com.ta.wan.R
import kotlinx.android.synthetic.main.fragment_home.*
import androidx.lifecycle.Observer

class HomeFragment : BaseFragment<HomeViewModel, ViewDataBinding>() {

    private val TAG = "StatusLayout"

    var isError = false
    var isEmpty = false

    override fun layoutId() = R.layout.fragment_home

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

    override fun handleStart() {
        isError = false
        isEmpty = false
        stateLayout.showLoadingView()
        Log.d(TAG, "showLoadingView")
    }

    override fun handleComplete() {
        if (!isError) {
            if (isEmpty) {
                stateLayout.showEmptyView()
                Log.d(TAG, "showEmptyView")
            } else {
                stateLayout.showContentView()
                Log.d(TAG, "showContentView")
            }
        }
    }

    override fun handleEvent(msg: Message) {
        isError = true
        stateLayout.showErrorView()
        Log.d(TAG, "showErrorView")
    }
}