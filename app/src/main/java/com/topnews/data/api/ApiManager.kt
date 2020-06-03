package com.topnews.data.api

import android.util.Log
import com.topnews.BuildConfig
import com.topnews.model.dailyfeedsresponse.DailyFeedsResponse
import com.topnews.model.topnewsourceresponse.TopNewSourcesResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by paras on 19/1/20.
 */
class ApiManager private constructor() {
    private val apiClient: ApiInterface
    private val httpClient: OkHttpClient.Builder

    init {
        apiClient = retrofitService
        httpClient = getHttpClient()
    }

    companion object {
        @JvmStatic
        val instance = ApiManager()

        private val retrofitService: ApiInterface
            get() {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BuildConfig.ENDPOINT)
                    .build()
                return retrofit.create(ApiInterface::class.java)
            }
    }
    /**
     * Method to create [OkHttpClient] builder by adding required headers in the [Request]
     *
     * @return OkHttpClient object
     */
    private fun getHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder: Request.Builder
                requestBuilder = original.newBuilder()
                    .method(original.method(), original.body())
                val request = requestBuilder.build()
                val response = chain.proceed(request)
                Log.e("Response =", response.message() + " // " + request.url())
                response
            }
            /*.authenticator(CustomAuthenticator())*/
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .writeTimeout(20000, TimeUnit.MILLISECONDS)
    }

    fun hitTopNewSources(): Call<TopNewSourcesResponse> {
        return apiClient.topNewSources(BuildConfig.API_KEY)
    }

    fun hitTopHeadlines(source: String): Call<DailyFeedsResponse> {
        return apiClient.topHeadlines(source, BuildConfig.API_KEY)
    }
}