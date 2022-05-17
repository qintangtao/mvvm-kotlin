package com.kotlin.mvvm.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kotlin.mvvm.ext.htmlToSpanned

object TextViewAdapter {

    @JvmStatic
    @BindingAdapter(value = ["html"], requireAll = false)
    fun setHtmlText(textView: TextView, text: String?) {
        text?.let {
            textView.setText(it.htmlToSpanned())
        }
    }

}