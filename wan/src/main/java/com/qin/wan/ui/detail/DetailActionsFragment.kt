package com.qin.wan.ui.detail

import android.os.Bundle
import android.view.View
import com.qin.wan.R
import com.qin.wan.model.bean.Article
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.qin.mvvm.base.BaseBottomSheetDialogFragment
import com.qin.mvvm.base.NoViewModel
import com.qin.mvvm.ext.copyTextIntoClipboard
import com.qin.mvvm.ext.openInExplorer
import com.qin.mvvm.ext.showToast
import com.qin.mvvm.utils.share
import com.qin.wan.databinding.FragmentDetailAcitonsBinding
import kotlinx.android.synthetic.main.fragment_detail_acitons.*

class DetailActionsFragment : BaseBottomSheetDialogFragment<NoViewModel, FragmentDetailAcitonsBinding>() {

    companion object {
        private const val PARAM_ARTICLE = "param_article"
        fun newInstance(article: Article): DetailActionsFragment {
            return DetailActionsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_ARTICLE, article)
                }
            }
        }
    }

    private var behavior: BottomSheetBehavior<View>? = null
    private lateinit var article: Article

    override fun layoutId() = R.layout.fragment_detail_acitons

    override fun onStart() {
        super.onStart()
        val bottomSheet: View = (dialog as BottomSheetDialog).delegate
            .findViewById(com.google.android.material.R.id.design_bottom_sheet)
            ?: return
        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun lazyLoadData() {
        arguments?.run {
            article  = getParcelable<Article>(PARAM_ARTICLE) ?: return@run
            mBinding?.article = article
            llCollect.setOnClickListener {
                (activity as? DetailActivity)?.changeCollect()
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
            llShare.setOnClickListener {
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                share(
                    activity!!,
                    activity!!.getString(R.string.app_name),
                    article.title + article.link
                )
            }
            llExplorer.setOnClickListener {
                openInExplorer(article.link)
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
            llCopy.setOnClickListener {
                context?.copyTextIntoClipboard(article.link, article.title)
                context?.showToast(R.string.copy_success)
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
            llRefresh.setOnClickListener {
                (activity as? DetailActivity)?.refreshPage()
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }


    fun show(manager: FragmentManager) {
        super.show(manager, DetailActionsFragment::class.java.name)
    }
}