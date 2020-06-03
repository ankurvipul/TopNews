package com.topnews.ui.home.topnewsources

import com.topnews.base.NetworkCallback
import com.topnews.base.RichMediatorLiveData
import com.topnews.data.DataManager
import com.topnews.model.FailureResponse
import com.topnews.model.topnewsourceresponse.TopNewSourcesResponse

class TopNewsSourcesRepo {

    /**
     * This method is used to hit TopNewSources API
     *
     * @param mTopNewSourcesLiveData live data object
     */
    fun getTopNewsSources(mTopNewSourcesLiveData: RichMediatorLiveData<TopNewSourcesResponse>) {
        DataManager.getInstance()!!.hitTopNewSources()
            .enqueue(object : NetworkCallback<TopNewSourcesResponse>() {
                override fun onSuccess(t: TopNewSourcesResponse?) {
                    mTopNewSourcesLiveData.value = t
                }

                override fun onFailure(failureResponse: FailureResponse) {
                    mTopNewSourcesLiveData.setFailure(failureResponse)
                }

                override fun onError(t: Throwable) {
                    mTopNewSourcesLiveData.setError(t)
                }
            })

    }
}