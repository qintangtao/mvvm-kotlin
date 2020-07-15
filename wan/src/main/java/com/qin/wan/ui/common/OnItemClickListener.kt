package com.qin.wan.ui.common

import android.view.View

interface OnItemClickListener<T> {
    fun onItemClick(view: View, item: T)
    fun onItemChildClick(view: View, item: T)
}