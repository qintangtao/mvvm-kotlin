package com.kotlin.mvvm.binding

import android.view.View
import androidx.databinding.BindingAdapter

object ViewAdapter {

    @JvmStatic
    @BindingAdapter(value = ["state_selected"], requireAll = false)
    fun setSelected(view: View, selected: Boolean) {
        view.isSelected = selected
    }

}