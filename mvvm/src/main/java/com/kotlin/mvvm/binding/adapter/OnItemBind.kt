package com.kotlin.mvvm.binding.adapter

interface OnItemBind<T> {

    fun onItemBind(
        itemBinding: ItemBinding<*>,
        position: Int,
        item: T?
    )
}