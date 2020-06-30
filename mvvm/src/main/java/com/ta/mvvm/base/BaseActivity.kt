package com.ta.mvvm.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.ta.mvvm.R
import com.ta.mvvm.event.Message
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var viewModel: VM

    protected var mBinding: DB? = null

    private var dialog: MaterialDialog? = null

    abstract fun layoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData()
    open fun handleStart() { showLoading() }
    open fun handleComplete() { dismissLoading() }
    open fun handleEvent(msg: Message) { ToastUtils.showLong("${msg.code}:${msg.msg}") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewDataBinding()
        lifecycle.addObserver(viewModel)
        registorDefUIChange()
        initView(savedInstanceState)
        initData()
    }

    private fun initViewDataBinding() {
        var cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            mBinding = DataBindingUtil.setContentView(this, layoutId())
            mBinding?.lifecycleOwner = this
        } else setContentView(layoutId())
        createViewModel()
    }

    private fun registorDefUIChange() {
        viewModel.defUI.start.observe(this, Observer {
            handleStart()
        })
        viewModel.defUI.complete.observe(this, Observer {
            handleComplete()
        })
        viewModel.defUI.error.observe(this, Observer {
            handleEvent(it)
        })
    }

    private fun showLoading() {
        if (dialog == null) {
            dialog = MaterialDialog(this)
                .cancelable(false)
                .cornerRadius(8f)
                .customView(R.layout.custom_progress_dialog_view, noVerticalPadding = true)
                .lifecycleOwner(this)
                .maxWidth(R.dimen.dialog_width)
        }
        dialog?.show()

    }

    private fun dismissLoading() {
        dialog?.run { if (isShowing) dismiss() }
    }

    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            var tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(tClass) as VM
        }
    }
}