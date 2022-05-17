package com.kotlin.wanandroid.ui.base

import com.blankj.utilcode.util.ActivityUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.wanandroid.model.api.ApiRetrofit
import com.kotlin.wanandroid.ui.common.UserRepository
import com.kotlin.wanandroid.ui.login.LoginActivity

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