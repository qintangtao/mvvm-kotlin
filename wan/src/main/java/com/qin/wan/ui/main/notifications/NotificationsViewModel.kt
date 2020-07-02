package com.qin.wan.ui.main.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qin.mvvm.base.BaseViewModel
import kotlinx.coroutines.delay

class NotificationsViewModel : BaseViewModel() {

    private val text = MutableLiveData<String>()

    fun testStatus(isNotify: Boolean = true): MutableLiveData<String> {
        launchGo({
            delay(2000)
            //throw ResponseThrowable(3000, "error ")
            text.value = "This is notifications Fragment"

            //This is dashboard Fragment
        }, isNotify = isNotify)
        return text
    }
}