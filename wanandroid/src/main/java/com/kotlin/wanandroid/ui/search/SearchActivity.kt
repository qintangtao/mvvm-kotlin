package com.kotlin.wanandroid.ui.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.base.NoViewModel
import com.kotlin.wanandroid.R
import com.kotlin.wanandroid.databinding.ActivitySearchBinding
import com.kotlin.wanandroid.ui.search.history.SearchHistoryFragment
import com.kotlin.wanandroid.ui.search.result.SearchResultFragment

class SearchActivity: BaseActivity<NoViewModel, ActivitySearchBinding>(){

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

        mBinding?.run {
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
    }

    override fun initData() { }

    fun fillSearchInput(keywords: String) {
        mBinding?.run {
            acetInput.setText(keywords)
            acetInput.setSelection(keywords.length)
        }
    }

    override fun onBackPressed() {
        mBinding?.run {
            ivBack.performClick()
        }
    }
}