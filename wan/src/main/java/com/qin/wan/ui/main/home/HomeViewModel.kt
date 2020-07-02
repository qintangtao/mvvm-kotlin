package com.qin.wan.ui.main.home

import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.base.BaseViewModel
import kotlinx.coroutines.delay

class HomeViewModel : BaseViewModel() {

    private val text = MutableLiveData<String>()

    fun testStatus(isNotify: Boolean = true): MutableLiveData<String> {
        launchGo({
            delay(2000)
            //throw ResponseThrowable(3000, "error ")
            text.value = ""

            //This is home Fragment
        }, isNotify = isNotify)
        return text
    }

}