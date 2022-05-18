package com.kotlin.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.event.Message
import com.kotlin.mvvm.ext.getContentLayout
import com.kotlin.mvvm.network.RESULT
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : BaseViewModel, DB : ViewBinding> : AppCompatActivity() {

    protected lateinit var viewModel: VM

    protected var mBinding: DB? = null

    private var dialog: MaterialDialog? = null

    abstract fun layoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewDataBinding()
        registorDefUIChange()
        initView(savedInstanceState)
        initData()
    }

    private fun initViewDataBinding() {
        var cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            //mBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
            var inflateMethod = cls.getMethod("inflate", LayoutInflater::class.java)
            mBinding = inflateMethod?.invoke(null, layoutInflater) as? DB
            (mBinding as? ViewDataBinding)?.lifecycleOwner = this
            //Log.d("BaseFragment", "BaseFragment2=============" + mBinding)
            setContentView(mBinding?.root)
        }
        else if (ViewBinding::class.java != cls && ViewBinding::class.java.isAssignableFrom(cls)) {
            var inflateMethod = cls.getMethod("inflate", LayoutInflater::class.java)
            mBinding = inflateMethod?.invoke(null, layoutInflater) as? DB
            //Log.d("BaseFragment", "BaseFragment3=============" + mBinding)
            setContentView(mBinding?.root)
        } else setContentView(layoutId())

/*
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            mBinding = DataBindingUtil.setContentView(this, layoutId())
            //mBinding?.lifecycleOwner = this
            (mBinding as ViewDataBinding).lifecycleOwner = this
        } else if (ViewBinding::class.java != cls && ViewBinding::class.java.isAssignableFrom(cls)) {
            mBinding = DataBindingUtil.setContentView(this, layoutId())
            //mBinding?.lifecycleOwner = this
        } else setContentView(layoutId())
 */

        createViewModel()
    }

    private fun registorDefUIChange() {
        viewModel.defUI.start.observe(this, Observer {
            onLoadStart()
        })
        viewModel.defUI.error.observe(this, Observer {
            onLoadEvent(it)
        })
        viewModel.defUI.result.observe(this, Observer {
            onLoadResult(it)
        })
        viewModel.defUI.complete.observe(this, Observer {
            onLoadCompleted()
        })
    }

    open fun onLoadStart() {
        showProgressDialog()
    }

    open fun onLoadEvent(msg: Message) {
        ToastUtils.showLong("${msg.code}:${msg.msg}")
    }

    open fun onLoadResult(code: Int) {
        when(code) {
            RESULT.END.code ->
                ToastUtils.showLong(RESULT.END.msg)
            RESULT.EMPTY.code ->
                ToastUtils.showLong(RESULT.EMPTY.msg)
            else -> {}
        }
    }

    open fun onLoadCompleted() {
        dismissProgressDialog()
    }

    fun showProgressDialog(resId: Int = R.string.now_loading) {
        if (dialog == null) {
            dialog = MaterialDialog(this)
                .cancelable(false)
                .cornerRadius(8f)
                .customView(R.layout.custom_progress_dialog_view, noVerticalPadding = true)
                .lifecycleOwner(this)
                .maxWidth(R.dimen.dialog_width)
            dialog?.getContentLayout().let {
                var tvTip = it?.findViewById(R.id.tvTip) as TextView ?: return@let
                tvTip.setText(resId)
            }
        }
        dialog?.show()
    }

    fun dismissProgressDialog() {
        dialog?.run { if (isShowing) dismiss() }
    }

    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            var tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(tClass) as VM
            lifecycle.addObserver(viewModel)
        }
    }
}