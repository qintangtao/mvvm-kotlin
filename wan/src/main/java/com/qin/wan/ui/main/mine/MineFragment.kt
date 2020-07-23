package com.qin.wan.ui.main.mine

import android.os.Bundle
import com.qin.mvvm.base.BaseFragment
import com.qin.mvvm.bus.Bus
import com.qin.wan.R
import com.qin.wan.databinding.FragmentMineBinding
import com.qin.wan.ui.common.UserRepository

class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>() {

    companion object {
        fun newInstance() = MineFragment()
    }

    override fun layoutId() = R.layout.fragment_mine

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mBinding?.viewModel = viewModel
        Bus.observe<Boolean>(UserRepository.USER_LOGIN_STATE_CHANGED, this) {
            viewModel.getUserInfo()
        }
    }

    override fun lazyLoadData() {
        viewModel.getUserInfo()
    }
}