package com.qin.wan.ui.main.home

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.qin.mvvm.text.htmlToSpanned

object ChapterAdapter {

    @JvmStatic
    @BindingAdapter(value = ["superChapterName", "chapterName"], requireAll = false)
    fun setHtmlText(textView: TextView, superChapterName: String?, chapterName: String?) {
        textView.text = when {
            !superChapterName.isNullOrEmpty() && !chapterName.isNullOrEmpty() ->
                "${superChapterName.htmlToSpanned()}/${chapterName.htmlToSpanned()}"
            !superChapterName.isNullOrEmpty() && chapterName.isNullOrEmpty() ->
                superChapterName.htmlToSpanned()
            superChapterName.isNullOrEmpty() && !chapterName.isNullOrEmpty() ->
                chapterName.htmlToSpanned()
            else -> ""
        }
    }

}