package com.kotlin.wanandroid

import com.kotlin.mvvm.base.BaseApplication
import com.kotlin.mvvm.utils.isMainProcess
import com.kotlin.mvvm.utils.setNightMode
import com.kotlin.wanandroid.model.store.SettingsStore

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        if (isMainProcess(this)) {
            setNightMode(SettingsStore.getNightMode())
        }
    }
}