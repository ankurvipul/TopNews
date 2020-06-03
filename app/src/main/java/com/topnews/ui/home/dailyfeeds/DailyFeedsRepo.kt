package com.topnews.ui.home.dailyfeeds

import com.topnews.base.NetworkCallback
import com.topnews.base.RichMediatorLiveData
import com.topnews.data.DataManager
import com.topnews.model.FailureResponse
import com.topnews.model.dailyfeedsresponse.DailyFeedsResponse
import com.topnews.model.topnewsourceresponse.TopNewSourcesResponse

class DailyFeedsRepo {
    /**
     * This method is used to hit TopHeadlines API
     *
     * @param mDailyFeedsLiveData live data object
     */
    fun getTopHeadlines(mDailyFeedsLiveData: RichMediatorLiveData<DailyFeedsResponse>, sources: String) {
        DataManager.getInstance()!!.hitTopHeadlines(sources)
            .enqueue(object : NetworkCallback<DailyFeedsResponse>() {
                override fun onSuccess(t: DailyFeedsResponse?) {
                    mDailyFeedsLiveData.value = t
                }

                override fun onFailure(failureResponse: FailureResponse) {
                    mDailyFeedsLiveData.setFailure(failureResponse)
                }

                override fun onError(t: Throwable) {
                    mDailyFeedsLiveData.setError(t)
                }
            })
    }
}