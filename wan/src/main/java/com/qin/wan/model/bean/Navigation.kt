package com.qin.wan.model.bean


data class Navigation(
    val cid: Int,
    val name: String,
    val articles: List<Article>
)