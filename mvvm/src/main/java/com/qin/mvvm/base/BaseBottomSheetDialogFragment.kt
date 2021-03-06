package com.qin.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.qin.mvvm.R
import com.qin.mvvm.event.Message
import com.qin.mvvm.ext.getContentLayout
import com.qin.mvvm.network.RESULT
import java.lang.reflect.ParameterizedType

abstract class BaseBottomSheetDialogFragment<VM : BaseViewModel, DB : ViewDataBinding> : BottomSheetDialogFragment() {

    protected lateinit var viewModel: VM

    protected var mBinding: DB? = null

    private var dialog: MaterialDialog? = null

    private var isFirstLoad: Boolean = true

    abstract fun layoutId(): Int
    open fun initView(savedInstanceState: Bundle?) {}
    open fun lazyLoadData() {}

    override fun show(manager: FragmentManager, tag: String?) {
        if (!this.isAdded) {
            super.show(manager, tag)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val cls =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            mBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
			mBinding?.lifecycleOwner = this
            return mBinding?.root
        }
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createViewModel()
		viewModel.let {
			lifecycle.addObserver(it)
		}
        registorDefUIChange()
        initView(savedInstanceState)
        onVisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isFirstLoad = true
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirstLoad) {
            lazyLoadData()
            isFirstLoad = false
        }
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
            dialog = MaterialDialog(activity!!)
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

    open fun isShareVM(): Boolean = false

    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            var tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            val viewModelStore = if (isShareVM()) activity!!.viewModelStore else this.viewModelStore
            viewModel = ViewModelProvider(viewModelStore, ViewModelProvider.NewInstanceFactory()).get(tClass) as VM
        }
    }

}