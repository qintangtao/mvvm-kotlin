package com.qin.mvvm.binding

import android.util.Log
import android.view.View
import android.widget.CheckedTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter

object ViewAdapter {

    @JvmStatic
    @BindingAdapter(value = ["state_selected"], requireAll = false)
    fun setSelected(view: View, selected: Boolean) {
        view.isSelected = selected
    }

    @JvmStatic
    @BindingAdapter(value = ["state_checked"], requireAll = false)
    fun setChecked(view: CheckedTextView, checked: Boolean) {
        view.isChecked = checked
    }

    @JvmStatic
    @BindingAdapter(value = ["state_checked"], requireAll = false)
    fun setChecked(view: SwitchCompat, checked: Boolean) {
        Log.d("SwitchCompat", "checked is " + checked)
        view.isChecked = checked
    }
}