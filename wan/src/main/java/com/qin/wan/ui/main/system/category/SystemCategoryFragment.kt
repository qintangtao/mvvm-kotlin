package com.qin.wan.ui.main.system.category


import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.ScreenUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.qin.mvvm.base.BaseBottomSheetDialogFragment
import com.qin.wan.R
import com.qin.wan.databinding.FragmentSystemCategoryBinding
import com.qin.wan.model.bean.Category

class SystemCategoryFragment() : BaseBottomSheetDialogFragment<SystemCategoryViewModel, FragmentSystemCategoryBinding>() {

    companion object {
        private const val PARAM_CATEGORY_LIST = "param_category_list"

        fun newInstance(categorys: ArrayList<Category>): SystemCategoryFragment {
            return  SystemCategoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(PARAM_CATEGORY_LIST, categorys)
                }
            }
        }
    }

    private var height: Int? = null
    private var behavior: BottomSheetBehavior<View>? = null
    private lateinit var categorys: ArrayList<Category>

    override fun layoutId() = R.layout.fragment_system_category

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mBinding?.viewModel = viewModel
        categorys = arguments?.getParcelableArrayList<Category>(PARAM_CATEGORY_LIST)!!
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet: View = (dialog as BottomSheetDialog).delegate
            .findViewById(com.google.android.material.R.id.design_bottom_sheet)
            ?: return
        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        height?.let { behavior?.peekHeight = it }
        dialog?.window?.let {
            it.setGravity(Gravity.BOTTOM)
            it.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, height ?: ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun lazyLoadData() {
        viewModel.items.value = categorys.toMutableList()
    }

    fun show(manager: FragmentManager, height: Int? = null) {
        this.height = height ?: (ScreenUtils.getScreenHeight() * 0.75f).toInt()
        super.show(manager, SystemCategoryFragment::class.java.name)
    }
}