package com.kotlin.mvvm.binding

import android.widget.EditText
import androidx.databinding.BindingAdapter

object EditTextAdapter {

    @JvmStatic
    @BindingAdapter(value = ["errorText"], requireAll = false)
    fun setErrorText(view: EditText, text: String?) {
        text?.let { view.error = it }
    }

}