package com.topnews.model.dailyfeedsresponse

data class DailyFeedsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)