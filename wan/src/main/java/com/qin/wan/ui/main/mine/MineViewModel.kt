package com.qin.wan.ui.main.mine

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.base.BaseViewModel
import com.qin.wan.R
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.model.bean.UserInfo
import com.qin.wan.ui.common.UserRepository
import com.qin.wan.ui.login.LoginActivity

class MineViewModel : BaseViewModel() {

    private val repository by lazy { UserRepository.getInstance(ApiRetrofit.getInstance()) }

    val userInfo = MutableLiveData<UserInfo?>()
    val isLogin = MutableLiveData<Boolean>()

    fun getUserInfo() {
        isLogin.value = repository.isLogin()
        userInfo.value = repository.getUserInfo()
    }

    fun click(view: View) {
        when(view.id) {
            R.id.clHeader -> checkLogin(view.context) {

            }
            R.id.llMyPoints -> checkLogin(view.context) {

            }
            R.id.llPointsRank -> checkLogin(view.context) {

            }
            R.id.llMyShare -> checkLogin(view.context) {

            }
            R.id.llMyCollect -> checkLogin(view.context) {

            }
            R.id.llHistory -> checkLogin(view.context) {

            }
            R.id.llAboutAuthor -> checkLogin(view.context) {

            }
            R.id.llOpenSource -> checkLogin(view.context) {

            }
            R.id.llSetting -> checkLogin(view.context) {

            }
        }
    }

    fun checkLogin(context: Context, block: (() -> Unit)? = null): Boolean {
        return if (repository.isLogin()) {
            block?.invoke()
            true
        } else {
            context.startActivity(Intent(context, LoginActivity::class.java))
            false
        }
    }
}