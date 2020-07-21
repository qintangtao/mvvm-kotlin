package com.qin.mvvm.binding.TagFlowLayout

import android.util.Log
import androidx.databinding.BindingAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import me.tatarka.bindingcollectionadapter2.ItemBinding

object BindingTagFlowLayoutAdapters {

    @JvmStatic
    @BindingAdapter(value = ["itemBinding", "items", "selected"], requireAll = false)
    fun  <T>  setAdapter(layout: TagFlowLayout, itemBinding: ItemBinding<T> , items: List<T>, selected: Int?) {
        requireNotNull(itemBinding) { "itemBinding must not be null" }
        val oldAdapter =  layout.adapter
        var adapter = oldAdapter as? BindingTagFlowLayoutAdapter<T> ?: BindingTagFlowLayoutAdapter<T>(items)
        adapter.itemBinding = itemBinding
        adapter.setItems(items.toMutableList())
        selected?.let {
            Log.d("selected", selected.toString())
            if (selected > -1) layout.adapter?.setSelectedList(selected)
        }
        layout.adapter = adapter
    }
}

