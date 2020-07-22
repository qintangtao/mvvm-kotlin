package com.qin.mvvm.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

object TextInputLayoutAdapter {

    @JvmStatic
    @BindingAdapter(value = ["errorText"], requireAll = false)
    fun setErrorText(view: TextInputLayout, text: String?) {
        text?.let {
            view.error = it
        }
    }

}