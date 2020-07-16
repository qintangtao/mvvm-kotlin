package com.qin.wan

import com.qin.mvvm.base.BaseApplication
import com.qin.mvvm.util.isMainProcess
import com.qin.mvvm.util.setNightMode
import com.qin.wan.model.store.SettingsStore

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        if (isMainProcess(this)) {
            setNightMode(SettingsStore.getNightMode())
        }
    }
}