package com.qin.mvvm.binding.TagFlowLayout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import me.tatarka.bindingcollectionadapter2.BindingCollectionAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding

class BindingTagFlowLayoutAdapter<T>(datas: List<T>) : TagAdapter<T>(datas),
    BindingCollectionAdapter<T> {

    private var inflater: LayoutInflater? = null
    private lateinit var items: List<T>
    private lateinit var itemBinding: ItemBinding<T>

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): T {
        return items.get(position)
    }

    override fun getView(parent: FlowLayout?, position: Int, t: T): View {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent!!.context)
        }
        val binding = onCreateBinding(inflater!!, itemBinding.layoutRes(), parent!!)
        val item = getItem(position)
        onBindBinding(binding, itemBinding.variableId(), itemBinding.layoutRes(), position, item)
        return binding.root
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        layoutId: Int,
        viewGroup: ViewGroup
    ): ViewDataBinding {
        return DataBindingUtil.inflate(inflater, layoutId, viewGroup, false)
    }

    override fun getItemBinding(): ItemBinding<T> {
        return itemBinding
    }

    override fun setItemBinding(itemBinding: ItemBinding<T>) {
        this.itemBinding = itemBinding
    }

    override fun setItems(items: MutableList<T>?) {
        if (this::items.isInitialized && this.items === items) {
            return
        }
        this.items = items!!
        notifyDataChanged()
    }

    override fun getAdapterItem(position: Int): T {
        return getItem(position)
    }

    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: T
    ) {
        if (itemBinding.bind(binding, item)) {
            binding.executePendingBindings()
        }
    }

}