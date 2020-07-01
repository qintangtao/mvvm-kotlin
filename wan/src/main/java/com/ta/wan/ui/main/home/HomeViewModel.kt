package com.ta.wan.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ta.mvvm.base.BaseViewModel
import com.ta.mvvm.network.ResponseThrowable
import kotlinx.coroutines.delay
import java.lang.RuntimeException

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