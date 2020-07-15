package com.qin.mvvm.binding

import android.widget.CheckedTextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.qin.mvvm.text.htmlToSpanned

object TextInputLayoutAdapter {

    @JvmStatic
    @BindingAdapter(value = ["errorText"], requireAll = false)
    fun setErrorText(view: TextInputLayout, text: String?) {
        text?.let {
            view.error = it
        }
    }

}