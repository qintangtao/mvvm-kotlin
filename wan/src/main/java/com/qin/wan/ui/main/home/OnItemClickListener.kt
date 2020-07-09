package com.qin.wan.ui.main.home

import com.qin.wan.model.bean.Article

interface OnItemClickListener {
    fun onItemClick(item: Article)
    fun onItemCollectClick(item: Article)
}