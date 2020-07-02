package com.qin.wan.ui.main.dashboard

import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.base.BaseViewModel
import com.qin.mvvm.network.ResponseThrowable
import kotlinx.coroutines.delay

class DashboardViewModel : BaseViewModel() {

    private val text = MutableLiveData<String>()

    fun testStatus(isNotify: Boolean = true): MutableLiveData<String> {
        launchGo({
            delay(2000)
            throw ResponseThrowable(3000, "error ")
            text.value = "This is dashboard Fragment"

            //This is dashboard Fragment
        }, isNotify = isNotify)
        return text
    }

}