package com.qin.wan.ui.main.system.category

import androidx.lifecycle.MutableLiveData
import com.qin.mvvm.BR
import com.qin.mvvm.base.BaseViewModel
import com.qin.wan.R
import com.qin.wan.model.bean.Category
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SystemCategoryViewModel : BaseViewModel() {

    val checked = MutableLiveData<Pair<Int, Int>>()

    val items = MutableLiveData<MutableList<Category>>()
    val itemBinding = ItemBinding.of<Category>(BR.itemBean, R.layout.item_system_category)
        .bindExtra(BR.viewModel, this)

    val itemBindingTag =  com.qin.mvvm.binding.adapter
        .ItemBinding.of<Category>(BR.itemBean, R.layout.item_system_category_tag)

}