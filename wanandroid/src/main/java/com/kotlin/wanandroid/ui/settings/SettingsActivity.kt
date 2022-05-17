package com.kotlin.wanandroid.ui.settings


import android.os.Bundle
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity<SettingsViewModel, ActivitySettingsBinding>() {

    companion object {
        fun newInstance() = SettingsActivity()
    }

    override fun layoutId() = R.layout.activity_settings

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel
        mBinding?.run {
            ivBack.setOnClickListener { finish() }
        }
    }

    override fun initData() {
        viewModel.initData()
    }


}