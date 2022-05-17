package com.kotlin.wanandroid.ui.login

import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.bus.Bus
import com.kotlin.mvvm.network.RESULT
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.model.api.ApiRetrofit
import com.kotlin.wanandroid.ui.common.UserRepository
import com.kotlin.wanandroid.ui.register.RegisterActivity

class LoginViewModel : BaseViewModel() {

    private val repository by lazy { UserRepository.getInstance(ApiRetrofit.getInstance()) }

    val account = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val accountError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    fun click() {
        accountError.value = ""
        passwordError.value = ""
        when {
            account.value.isNullOrEmpty() -> accountError.value = getString(R.string.account_can_not_be_empty)
            password.value.isNullOrEmpty() -> passwordError.value = getString(R.string.password_can_not_be_empty)
            else -> {
                launchOnlyResult({
                    repository.login(account.value!!, password.value!!)
                }, {
                    //save userinfo
                    repository.updateUserInfo(it)
                    Bus.post(UserRepository.USER_LOGIN_STATE_CHANGED, true)
                    RESULT.END.code
                })
            }
        }
    }

    fun clickRegister(view: View) {
        view.context.startActivity(Intent(view.context, RegisterActivity::class.java))
    }
}