package com.topnews.data

import android.content.Context
import com.topnews.data.api.ApiManager
import com.topnews.model.dailyfeedsresponse.DailyFeedsResponse
import com.topnews.model.topnewsourceresponse.TopNewSourcesResponse
import retrofit2.Call

class DataManager constructor(context: Context) {
    private var apiManager: ApiManager? = null
    /**
     * Method to initialize [ApiManager] class
     */
    fun initApiManager() {
        apiManager = ApiManager.instance
    }

    companion object {
        private var instance: DataManager? = null
        /**
         * Returns the single instance of [DataManager] if
         * [.init] is called first
         *
         * @return instance
         */
        @JvmStatic
        fun getInstance(): DataManager? {
            checkNotNull(instance) { "Call init() before getInstance()" }
            return instance
        }

        /**
         * Method used to create an instance of [DataManager]
         *
         * @param context of the application passed from the [BaseMVVMSample]
         * @return instance if it is null
         */
        @JvmStatic
        @Synchronized
        fun init(context: Context): DataManager? {
            if (instance == null) {
                instance = DataManager(context)
            }
            return instance
        }
    }

    fun hitTopNewSources(): Call<TopNewSourcesResponse> {
        return apiManager!!.hitTopNewSources()
    }

    fun hitTopHeadlines(source: String): Call<DailyFeedsResponse> {
        return apiManager!!.hitTopHeadlines(source)
    }
}