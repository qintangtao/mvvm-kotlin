package com.qin.mvvm.base

import android.view.View

interface OnItemClickListener<T> {
    fun onClick(view: View, item: T)
}