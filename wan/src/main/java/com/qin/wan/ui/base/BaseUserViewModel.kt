package com.qin.wan.ui.base

import com.blankj.utilcode.util.ActivityUtils
import com.qin.mvvm.base.BaseViewModel
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.ui.common.UserRepository
import com.qin.wan.ui.login.LoginActivity

open class BaseUserViewModel : BaseViewModel() {

    protected val repository by lazy { UserRepository.getInstance(ApiRetrofit.getInstance()) }

    fun checkLogin(block: (() -> Unit)? = null): Boolean {
        return if (repository.isLogin()) {
            block?.invoke()
            true
        } else {
            ActivityUtils.startActivity(LoginActivity::class.java)
            false
        }
    }
}