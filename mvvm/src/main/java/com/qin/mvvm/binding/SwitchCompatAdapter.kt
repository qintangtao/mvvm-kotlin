package com.qin.mvvm.binding

import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter

object SwitchCompatAdapter {

    @JvmStatic
    @BindingAdapter(value = ["switchClick"], requireAll = false)
    fun switchClick(sc: SwitchCompat, listener: CompoundButton.OnCheckedChangeListener) {
        sc.setOnCheckedChangeListener(listener)
    }

}