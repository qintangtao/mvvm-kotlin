package com.qin.wan.ui.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.databinding.ViewDataBinding
import com.qin.mvvm.base.BaseActivity
import com.qin.mvvm.base.NoViewModel
import com.qin.wan.R
import com.qin.wan.ui.search.history.SearchHistoryFragment
import com.qin.wan.ui.search.result.SearchResultFragment
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity: BaseActivity<NoViewModel, ViewDataBinding>(){

    override fun layoutId() = R.layout.activity_search

    override fun initView(savedInstanceState: Bundle?) {
        val historyFragment = SearchHistoryFragment.newInstance()
        val resultFragment = SearchResultFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.flContainer, historyFragment)
            .add(R.id.flContainer, resultFragment)
            .show(historyFragment)
            .hide(resultFragment)
            .commit()

        ivBack.setOnClickListener {
            if (resultFragment.isVisible) {
                supportFragmentManager
                    .beginTransaction()
                    .show(historyFragment)
                    .hide(resultFragment)
                    .commit()
            } else {
                finish()
            }
        }

        ivDone.setOnClickListener {
            val searchWords = acetInput.text.toString()
            if (searchWords.isEmpty()) return@setOnClickListener
            //it.hideSoftInput()
            historyFragment.addSearchHistory(searchWords)
            resultFragment.search(searchWords)
            supportFragmentManager
                .beginTransaction()
                .hide(historyFragment)
                .show(resultFragment)
                .commit()
        }

        acetInput.run {
            addTextChangedListener(afterTextChanged = {
                ivClearSearch.isGone = it.isNullOrEmpty()
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ivDone.performClick()
                    true
                } else {
                    false
                }
            }
        }

        ivClearSearch.setOnClickListener { acetInput.setText("") }
    }

    override fun initData() { }

    fun fillSearchInput(keywords: String) {
        acetInput.setText(keywords)
        acetInput.setSelection(keywords.length)
    }

    override fun onBackPressed() {
        ivBack.performClick()
    }
}