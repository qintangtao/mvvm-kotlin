package com.qin.mvvm.binding

import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter

object SwitchCompatAdapter {

    @JvmStatic
    @BindingAdapter(value = ["onCheckedChange"], requireAll = false)
    fun switchClick(sc: SwitchCompat, listener: CompoundButton.OnCheckedChangeListener) {
        sc.setOnCheckedChangeListener(listener)
    }

    @JvmStatic
    @BindingAdapter(value = ["state_checked"], requireAll = false)
    fun setChecked(sc: SwitchCompat, checked: Boolean) {
        sc.isChecked = checked
    }
}