package com.qin.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.qin.mvvm.R
import com.qin.mvvm.event.Message
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment() {

    protected lateinit var viewModel: VM

    protected var mBinding: DB? = null

    private var dialog: MaterialDialog? = null

    private var isFirstLoad: Boolean = true

    abstract fun layoutId(): Int
    open fun initView(savedInstanceState: Bundle?) {}
    open fun lazyLoadData() {}

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
		viewModel?.let {
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
        viewModel?.defUI.start.observe(this, Observer {
            onLoadStart()
        })
        viewModel?.defUI.error.observe(this, Observer {
            onLoadEvent(it)
        })
        viewModel?.defUI.empty.observe(this, Observer {
            onLoadEmpty()
        })
        viewModel?.defUI.complete.observe(this, Observer {
            onLoadCompleted()
        })
    }

    open fun onLoadStart() {
        if (dialog == null) {
            dialog = context?.let {
                MaterialDialog(it)
                    .cancelable(false)
                    .cornerRadius(8f)
                    .customView(R.layout.custom_progress_dialog_view, noVerticalPadding = true)
                    .lifecycleOwner(this)
                    .maxWidth(R.dimen.dialog_width)
            }
        }
        dialog?.show()
    }

    open fun onLoadEvent(msg: Message) {
        ToastUtils.showLong("${msg.code}:${msg.msg}")
    }

    open fun onLoadEmpty() {
        ToastUtils.showLong(R.string.state_empty)
    }

    open fun onLoadCompleted() {
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