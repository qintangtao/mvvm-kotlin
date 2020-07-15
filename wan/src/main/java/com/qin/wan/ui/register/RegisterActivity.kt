package com.qin.wan.ui.register

import android.os.Bundle
import com.qin.mvvm.base.BaseActivity
import com.qin.mvvm.event.Message
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity<RegisterViewModel, ActivityRegisterBinding>(){

    override fun layoutId() = R.layout.activity_register

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel
        ivBack.setOnClickListener { finish() }
    }

    override fun initData() {

    }

    override fun onLoadStart() {
        showProgressDialog(R.string.registerring)
    }

    override fun onLoadResult(code: Int) {
        when(code) {
            RESULT.END.code -> finish()
            else -> super.onLoadResult(code)
        }
    }

    override fun onLoadEvent(msg: Message) {
        viewModel.confirmPasswordError.value = msg.msg
    }
}