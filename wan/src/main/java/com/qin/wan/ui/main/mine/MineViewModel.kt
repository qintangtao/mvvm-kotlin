package com.qin.wan.ui.main.mine

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ActivityUtils
import com.qin.wan.R
import com.qin.wan.model.bean.UserInfo
import com.qin.wan.ui.base.BaseUserViewModel
import com.qin.wan.ui.settings.SettingsActivity

class MineViewModel : BaseUserViewModel() {

    val userInfo = MutableLiveData<UserInfo?>()
    val isLogin = MutableLiveData<Boolean>()

    fun getUserInfo() {
        isLogin.value = repository.isLogin()
        userInfo.value = repository.getUserInfo()
    }

    fun click(view: View) {
        when(view.id) {
            R.id.clHeader -> checkLogin {

            }
            R.id.llMyPoints -> checkLogin {

            }
            R.id.llPointsRank -> checkLogin {

            }
            R.id.llMyShare -> checkLogin {

            }
            R.id.llMyCollect -> checkLogin {

            }
            R.id.llHistory -> checkLogin {

            }
            R.id.llAboutAuthor -> checkLogin {

            }
            R.id.llOpenSource -> checkLogin {

            }
            R.id.llSetting -> checkLogin {
                ActivityUtils.startActivity(SettingsActivity::class.java)
            }
        }
    }
}