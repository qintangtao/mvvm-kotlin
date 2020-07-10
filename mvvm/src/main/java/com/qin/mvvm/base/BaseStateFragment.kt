package com.qin.mvvm.base

import android.os.Parcel
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.qin.mvvm.view.StateLayout
import com.qin.mvvm.event.Message
import com.qin.mvvm.network.ERROR
import com.qin.mvvm.network.RESULT

abstract class BaseStateFragment<VM : BaseViewModel, DB : ViewDataBinding>() : BaseFragment<VM, DB>() {

    private val TAG = "StatusLayout"
    private var isErrorLoaded = false
    private var isEmptyLoaded = false

    constructor(parcel: Parcel) : this() {
        isErrorLoaded = parcel.readByte() != 0.toByte()
        isEmptyLoaded = parcel.readByte() != 0.toByte()
    }

    abstract fun stateLayout(): StateLayout

    /*
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
*/
    override fun onResume() {
        super.onResume()
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

    override fun onLoadStart() {
        isErrorLoaded = false
        isEmptyLoaded = false
        stateLayout().showLoadingView()
        Log.d(TAG, "showLoadingView")
    }

    override fun onLoadEvent(msg: Message) {
        isErrorLoaded = true
        if (msg.code == ERROR.UNKNOWN.getCode())
            stateLayout().showErrorView()
        else
            stateLayout().showNetErrorView()
        Log.d(TAG, "showErrorView")
    }

    override fun onLoadResult(code: Int) {
        when(code) {
            RESULT.EMPTY.code -> {
                isEmptyLoaded = true
                stateLayout().showEmptyView()
                Log.d(TAG, "showEmptyView")
            }
            else -> {}
        }
    }

    override fun onLoadCompleted() {
        if (!isErrorLoaded && !isEmptyLoaded) {
            stateLayout().showContentView()
            Log.d(TAG, "showContentView")
        }
    }

}