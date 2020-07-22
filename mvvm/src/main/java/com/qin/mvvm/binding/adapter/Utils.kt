package com.qin.mvvm.binding.adapter

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

object Utils {

    fun throwMissingVariable(
        binding: ViewDataBinding,
        bindingVariable: Int,
        @LayoutRes layoutRes: Int
    ) {
        val context = binding.root.context
        val resources = context.resources
        val layoutName = resources.getResourceName(layoutRes)
        val bindingVariableName =
            DataBindingUtil.convertBrIdToString(bindingVariable)
        throw IllegalStateException("Could not bind variable '$bindingVariableName' in layout '$layoutName'")
    }

}