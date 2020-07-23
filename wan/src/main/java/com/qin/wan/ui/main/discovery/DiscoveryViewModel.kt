package com.qin.wan.ui.main.discovery

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ResourceUtils
import com.qin.mvvm.BR
import com.qin.mvvm.network.ExceptionHandle
import com.qin.mvvm.network.RESULT
import com.qin.wan.R
import com.qin.wan.model.api.ApiRetrofit
import com.qin.wan.model.bean.Article
import com.qin.wan.model.bean.Banner
import com.qin.wan.model.bean.Frequently
import com.qin.wan.model.bean.HotWord
import com.qin.wan.ui.base.BaseUserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.lang.RuntimeException


class DiscoveryViewModel : BaseUserViewModel() {

    private val disRepository by lazy { DiscoveryRepository.getInstance(ApiRetrofit.getInstance()) }

    val banners = MutableLiveData<MutableList<Banner>>()

    val hotWordItems = MutableLiveData<MutableList<HotWord>>()
    val hotWordItemBinding = ItemBinding.of<HotWord>(BR.itemBean, R.layout.item_hot_word)

    val frequentlyItems = MutableLiveData<MutableList<Frequently>>()
    val frequentlyItemBinding = com.qin.mvvm.binding.adapter
        .ItemBinding.of<Frequently>(BR.itemBean, R.layout.item_nav_tag)


    fun getData(isNotify: Boolean = false) {
        /*
        launchUI {
            launchFlow { Log.d("launchFlow", "block")
                333}
                .retry(3)
                .onStart { Log.d("launchFlow", "onStart") }
                .flowOn(Dispatchers.IO)
                .onEach { Log.d("launchFlow", "onEach")
                    //throw RuntimeException("onEach")
                    555
                }
                .catch { Log.d("launchFlow", "catch: " + it.message)}
                .onCompletion { Log.d("launchFlow", "onCompletion") }
                .collect { Log.d("launchFlow", "collect " + it)}
        }
*/
        if (isNotify) callStart()
        launchOnlyResult({
            disRepository.getBanners()
        },{
            if (it.isNullOrEmpty()) RESULT.EMPTY.code
            else {
                banners.value = it.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = false)

        launchOnlyResult({
            disRepository.getHotWords()
        },{
            if (it.isNullOrEmpty()) RESULT.EMPTY.code
            else {
                hotWordItems.value = it.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = false)

        launchOnlyResult({
            disRepository.getFrequentlyWebsites()
        },{
            if (it.isNullOrEmpty()) RESULT.EMPTY.code
            else {
                frequentlyItems.value = it.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = false)
    }
}