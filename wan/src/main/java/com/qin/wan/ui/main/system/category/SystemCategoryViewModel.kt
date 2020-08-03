package com.qin.wan.ui.main.system.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.BR
import com.qin.mvvm.base.BaseViewModel
import com.qin.mvvm.bus.Bus
import com.qin.wan.R
import com.qin.wan.model.bean.Category
import com.zhy.view.flowlayout.TagFlowLayout
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SystemCategoryViewModel : BaseViewModel() {

    private val _checked = MutableLiveData<Pair<Int, Int>>()
    private val _items = MutableLiveData<MutableList<Category>>()

    val checked: LiveData<Pair<Int, Int>> = _checked
    val items: LiveData<MutableList<Category>> = _items

    val itemBinding = ItemBinding.of<Category>(BR.itemBean, R.layout.item_system_category)
        .bindExtra(BR.viewModel, this)

    val itemBindingTag =  com.qin.mvvm.binding.adapter
        .ItemBinding.of<Category>(BR.itemBean, R.layout.item_system_category_tag)

    val onTagClickListener = TagFlowLayout.OnTagClickListener { view, position, parent ->
        var id = parent.getTag() as Int
        val item = items.value!!.find { it.id == id } ?: return@OnTagClickListener false
        id = items.value!!.indexOf(item)
        _checked.value = id to position
        Bus.post("CHECK",id to position)
        true
    }

    fun initData(chacked: Pair<Int, Int>, categorys: List<Category>) {
        _checked.value = chacked
        _items.value = categorys.toMutableList()
    }
}