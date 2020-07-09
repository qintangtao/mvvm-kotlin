package com.qin.wan.ui.main.notifications

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.base.BaseViewModel
import kotlinx.coroutines.delay
import androidx.databinding.adapters.TextViewBindingAdapter
import com.qin.mvvm.network.ResponseThrowable

class NotificationsViewModel : BaseViewModel() {

    val text = MutableLiveData<String>()

    //var navTitle = ArrayList<String>()
    var navTitle = MutableLiveData<List<String>>()

    //var navTitle = ObservableArrayList<String>()

    fun testStatus(isNotify: Boolean = true): MutableLiveData<String> {
        launchGo({
            delay(1000)
            throw ResponseThrowable(3000, "error ")
            text.value = "This is notifications Fragment"

            navTitle.value = arrayListOf<String>("首页","新闻","历史","我")
            /*
            arrayListOf<String>("首页","新闻","历史","我").forEach {
                navTitle.add(it)
            }*/

            //This is dashboard Fragment
        }, isNotify = isNotify)
        return text
    }
}