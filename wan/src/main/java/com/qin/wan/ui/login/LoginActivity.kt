package com.qin.wan.ui.login

import android.os.Bundle
import com.qin.mvvm.base.BaseActivity
import com.qin.mvvm.event.Message
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>(){

    override fun layoutId() = R.layout.activity_login

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel
        ivClose.setOnClickListener { finish() }
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