package com.qin.mvvm.binding.adapter.TagFlowLayout

import androidx.databinding.BindingAdapter
import com.qin.mvvm.binding.adapter.ItemBinding
import com.zhy.view.flowlayout.TagFlowLayout

object BindingTagFlowLayoutAdapters {

    @JvmStatic
    @BindingAdapter(value = ["itemBinding", "items", "selected"], requireAll = false)
    fun  <T>  setAdapter(layout: TagFlowLayout, itemBinding: ItemBinding<T>, items: List<T>, selected: Int?) {
        requireNotNull(itemBinding) { "itemBinding must not be null" }
        val oldAdapter =  layout.adapter
        var adapter = oldAdapter as? BindingTagFlowLayoutAdapter<T> ?: BindingTagFlowLayoutAdapter<T>(items)
        adapter.setItemBinding(itemBinding)
        adapter.setItems(items.toMutableList())
        selected?.let {
            if (selected > -1) layout.adapter?.setSelectedList(selected)
        }
        layout.adapter = adapter
    }
}

