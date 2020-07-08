package com.qin.wan.model.bean


data class Shared(
    val coinInfo: PointRank,
    val shareArticles: Pagination<Article>
)