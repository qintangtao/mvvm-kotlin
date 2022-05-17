package com.kotlin.wanandroid.ui.login

import android.os.Bundle
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.event.Message
import com.kotlin.mvvm.network.RESULT
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>(){

    override fun layoutId() = R.layout.activity_login

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel
        mBinding!!.ivClose.setOnClickListener { finish() }
    }

    override fun initData() {
        viewModel.account.value = "tangtao"
        viewModel.password.value = "123456"
    }

    override fun onLoadStart() {
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