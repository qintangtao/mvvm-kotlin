package com.qin.mvvm.base

import android.os.Bundle
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.qin.mvvm.view.StateLayout
import com.qin.mvvm.event.Message
import com.qin.mvvm.network.ERROR

abstract class BaseStateFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseFragment<VM, DB>() {

    private val TAG = "StatusLayout"
    private var isError = false
    private var isEmpty = false

    abstract fun stateLayout(): StateLayout

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        stateLayout().run {
            setEmptyClickListener{
                lazyLoadData()
            }
            setErrorClickListener{
                lazyLoadData()
            }
            setNetErrorClickListener{
                lazyLoadData()
            }
        }
    }

    override fun handleStart() {
        isError = false
        isEmpty = false
        stateLayout().showLoadingView()
        Log.d(TAG, "showLoadingView")
    }

    override fun handleEvent(msg: Message) {
        isError = true
        if (msg.code == ERROR.UNKNOWN.getCode())
            stateLayout().showErrorView()
        else
            stateLayout().showNetErrorView()
        Log.d(TAG, "showErrorView")
    }

    override fun handleEmpty() {
        isEmpty = true
        stateLayout().showEmptyView()
        Log.d(TAG, "showEmptyView")
    }

    override fun handleComplete() {
        if (!isError && !isEmpty) {
            stateLayout().showContentView()
            Log.d(TAG, "showContentView")
        }
    }

}