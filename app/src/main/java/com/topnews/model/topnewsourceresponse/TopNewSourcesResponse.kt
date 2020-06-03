package com.topnews.model.topnewsourceresponse

data class TopNewSourcesResponse(
    val sources: List<Source>,
    val status: String
)