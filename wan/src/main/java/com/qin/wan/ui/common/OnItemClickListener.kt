package com.qin.wan.ui.common

import android.view.View

interface OnItemClickListener<T> {
    fun onItemClick(item: T)
    fun onItemChildClick(view: View, item: T)
}