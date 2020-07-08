package com.qin.wan.ui.main.dashboard

import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.base.BaseViewModel
import kotlinx.coroutines.delay
import org.json.JSONException

class DashboardViewModel : BaseViewModel() {

    val text = MutableLiveData<String>()

    fun testStatus(isNotify: Boolean = false) {
        launchGo({
            delay(1000)
            throw JSONException("TAG error")
            //throw ResponseThrowable(3000, "error ")
            //text.value = "This is dashboard Fragment"
            text.value = "This is dashboard Fragment"
            //This is dashboard Fragment
        }, isNotify = isNotify)
    }

}