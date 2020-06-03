package com.topnews.data.api

import com.topnews.model.dailyfeedsresponse.DailyFeedsResponse
import com.topnews.model.topnewsourceresponse.TopNewSourcesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by paras on 19/1/20.
 */
interface ApiInterface {

    @GET("sources")
    fun topNewSources(@Query("apiKey") apiKey: String): Call<TopNewSourcesResponse>

    @GET("top-headlines")
    fun topHeadlines(@Query("sources") sourceId: String,
                     @Query("apiKey") apiKey: String): Call<DailyFeedsResponse>

}