package com.kotlin.wanandroid.ui.register

import android.os.Bundle
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.event.Message
import com.kotlin.mvvm.network.RESULT
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity<RegisterViewModel, ActivityRegisterBinding>(){

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel
        mBinding.ivBack.setOnClickListener { finish() }
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