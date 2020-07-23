package com.qin.wan.ui.register

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ResourceUtils
import com.qin.mvvm.base.BaseViewModel
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.ui.common.UserRepository

class RegisterViewModel : BaseViewModel() {
    private val repository by lazy { UserRepository.getInstance(ApiRetrofit.getInstance()) }

    val account = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    val accountError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()
    val confirmPasswordError = MutableLiveData<String>()

    @SuppressLint("ResourceType")
    fun click() {
        accountError.value = ""
        passwordError.value = ""
        confirmPasswordError.value = ""
        when {
            account.value.isNullOrEmpty() -> accountError.value = ResourceUtils.readRaw2String(R.string.account_can_not_be_empty)
            account.value!!.length < 3 ->  accountError.value = ResourceUtils.readRaw2String(R.string.account_length_over_three)
            password.value.isNullOrEmpty() -> passwordError.value = ResourceUtils.readRaw2String(R.string.password_can_not_be_empty)
            password.value!!.length < 6 ->  passwordError.value = ResourceUtils.readRaw2String(R.string.password_length_over_six)
            confirmPassword.value.isNullOrEmpty() -> confirmPasswordError.value = ResourceUtils.readRaw2String(R.string.confirm_password_can_not_be_empty)
            !password.value.equals(confirmPassword.value) -> confirmPasswordError.value = ResourceUtils.readRaw2String(R.string.two_password_are_inconsistent)
            else -> {
                launchOnlyResult({
                    repository.register(account.value!!, password.value!!, confirmPassword.value!!)
                }, {
                    RESULT.END.code
                })
            }
        }
    }
}