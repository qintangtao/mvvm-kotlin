package com.kotlin.wanandroid.ui.main.mine

import android.os.Bundle
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.bus.Bus
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.databinding.FragmentMineBinding
import com.kotlin.wanandroid.ui.common.UserRepository
class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>() {

    companion object {
        fun newInstance() = MineFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mBinding.viewModel = viewModel
        Bus.observe<Boolean>(UserRepository.USER_LOGIN_STATE_CHANGED, this) {
            viewModel.requestUserInfo()
        }
    }

    override fun lazyLoadData() {
        viewModel.requestUserInfo()
    }
}