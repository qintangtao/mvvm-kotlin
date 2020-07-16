package com.qin.wan.ui.settings


import android.os.Bundle
import com.qin.mvvm.base.BaseActivity
import com.qin.mvvm.util.isNightMode
import com.qin.wan.R
import com.qin.wan.databinding.ActivitySettingsBinding
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity<SettingsViewModel, ActivitySettingsBinding>() {

    companion object {
        fun newInstance() = SettingsActivity()
    }

    override fun layoutId() = R.layout.activity_settings

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel
        ivBack.setOnClickListener { finish() }
    }

    override fun initData() {
        viewModel.initData()
        viewModel.isNight.value = isNightMode(this)
    }


}