package com.qin.mvvm.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.qin.mvvm.text.htmlToSpanned

object TextViewAdapter {

    @JvmStatic
    @BindingAdapter(value = ["html"], requireAll = false)
    fun setHtmlText(textView: TextView, text: String?) {
        text?.let {
            textView.setText(it.htmlToSpanned())
        }
    }

}