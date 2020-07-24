package com.qin.mvvm.binding.adapter

import android.util.Log
import android.util.SparseArray
import androidx.annotation.LayoutRes
import androidx.core.util.forEach
import androidx.databinding.ViewDataBinding

class ItemBinding<T> private constructor(
    private var onItemBind: OnItemBind<T>? = null){

    companion object {
        val VAR_NONE = 0
        private val VAR_INVALID = -1
        private val LAYOUT_NONE = 0

        fun <T> of(
            variableId: Int,
            @LayoutRes layoutRes: Int
        ): ItemBinding<T> {
            return ItemBinding<T>().set(variableId, layoutRes)
        }

        fun <T> of(onItemBind: OnItemBind<T>): ItemBinding<T> {
            requireNotNull(onItemBind) { "onItemBind == null" }
            return ItemBinding(onItemBind)
        }

    }

    private var variableId: Int = 0
    @LayoutRes private var layoutRes: Int = 0
    private var extraBindings: SparseArray<Any>? = null


    fun set(
        variableId: Int,
        @LayoutRes layoutRes: Int
    ): ItemBinding<T> {
        this.variableId = variableId
        this.layoutRes = layoutRes
        return this
    }

    fun variableId(variableId: Int): ItemBinding<T> {
        this.variableId = variableId
        return this
    }
    fun layoutRes(@LayoutRes layoutRes: Int): ItemBinding<T> {
        this.layoutRes = layoutRes
        return this
    }

    fun bindExtra(
        variableId: Int,
        value: Any?
    ): ItemBinding<T> {
        extraBindings ?: SparseArray<Any>(1).also { extraBindings = it }
        extraBindings?.put(variableId, value)
        return this
    }

    fun clearExtras(): ItemBinding<T> {
        extraBindings?.clear()
        return this
    }

    fun removeExtra(variableId: Int): ItemBinding<T>? {
        extraBindings?.remove(variableId)
        return this
    }

    fun variableId() = variableId
    fun layoutRes() = layoutRes

    fun extraBinding(variableId: Int): Any? {
        return extraBindings?.get(variableId)
    }

    fun onItemBind(position: Int, item: T) {
        onItemBind?.let {
            variableId = ItemBinding.VAR_INVALID
            layoutRes =  ItemBinding.LAYOUT_NONE
            it.onItemBind(this, position, item)
            check(variableId != ItemBinding.VAR_INVALID) { "variableId not set in onItemBind()" }
            check(layoutRes != ItemBinding.LAYOUT_NONE) { "layoutRes not set in onItemBind()" }
        }
    }

    fun bind(binding: ViewDataBinding, item: T): Boolean {
        if (variableId == ItemBinding.VAR_NONE) {
            return false
        }
        val result = binding.setVariable(variableId, item)
        if (!result) {
            Utils.throwMissingVariable(binding, variableId, layoutRes)
        }
        extraBindings?.forEach { key, value ->
            if (key != ItemBinding.VAR_NONE) {
                binding.setVariable(key, value)
                Log.d("NavigationViewModel", "key " + key + "  value " + value)
            }
        }
        return true
    }
}