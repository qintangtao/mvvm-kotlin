package com.kotlin.wanandroid.ui.common

import com.kotlin.mvvm.base.BaseModel
import com.kotlin.wanandroid.model.api.ApiRetrofit
import com.kotlin.wanandroid.model.bean.UserInfo
import com.kotlin.wanandroid.model.store.UserInfoStore

class UserRepository private constructor(
    private val remote: ApiRetrofit
// private val local: ApiRoom
) : BaseModel() {

    suspend fun login(username: String, password: String) =  remote.service.login(username, password)
    suspend fun register(username: String, password: String, repassword: String) = remote.service.register(username, password, repassword)

    fun updateUserInfo(userInfo: UserInfo) = UserInfoStore.setUserInfo(userInfo)

    fun isLogin() = UserInfoStore.isLogin()

    fun getUserInfo() = UserInfoStore.getUserInfo()

    fun clearLoginState() {
        UserInfoStore.clearUserInfo()
        //RetrofitClient.clearCookie()
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(remote: ApiRetrofit) =
            INSTANCE ?: synchronized(this) {
            INSTANCE ?: UserRepository(remote).also { INSTANCE = it }
        }

        const val USER_LOGIN_STATE_CHANGED = "user_login_state_changed"
    }
}