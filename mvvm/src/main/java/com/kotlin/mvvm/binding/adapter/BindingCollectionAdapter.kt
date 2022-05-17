package com.kotlin.mvvm.binding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

interface BindingCollectionAdapter<T> {

    fun getItemBinding(): ItemBinding<T>
    fun setItemBinding(itemBinding: ItemBinding<T>)

    fun setItems(items: List<T>?)

    fun getAdapterItem(position: Int): T

    fun onCreateBinding(
        inflater: LayoutInflater,
        @LayoutRes layoutRes: Int,
        viewGroup: ViewGroup
    ): ViewDataBinding?

    fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        @LayoutRes layoutRes: Int,
        position: Int,
        item: T
    )
}