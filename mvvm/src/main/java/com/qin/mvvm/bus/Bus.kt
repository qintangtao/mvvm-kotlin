package com.qin.mvvm.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus

object Bus {

    inline fun <reified T> post(key: String, value: T) =
        LiveEventBus.get(key, T::class.java).post(value)

    inline fun <reified T> observe(
        key: String,
        lifecycleOwner: LifecycleOwner,
        crossinline observer: (T) -> Unit
    ) = LiveEventBus.get(key, T::class.java).observe(lifecycleOwner, Observer { observer(it) })
}