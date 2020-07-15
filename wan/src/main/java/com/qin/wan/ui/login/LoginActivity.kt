package com.qin.wan.ui.login

import android.os.Bundle
import com.qin.mvvm.base.BaseActivity
import com.qin.mvvm.event.Message
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>(){

    override fun layoutId() = R.layout.activity_login

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel
    }

    override fun initData() {

    }

    override fun onLoadStart() {
        viewModel.accountError.value = ""
        viewModel.passwordError.value = ""
        showProgressDialog(R.string.logging_in)
    }

    override fun onLoadResult(code: Int) {
        when(code) {
            RESULT.END.code -> finish()
            else -> super.onLoadResult(code)
        }
    }

    override fun onLoadEvent(msg: Message) {
        viewModel.passwordError.value = msg.msg
    }
}