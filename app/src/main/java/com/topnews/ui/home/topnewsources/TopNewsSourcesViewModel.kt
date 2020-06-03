package com.topnews.ui.home.topnewsources

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.topnews.base.RichMediatorLiveData
import com.topnews.model.FailureResponse
import com.topnews.model.topnewsourceresponse.TopNewSourcesResponse
import com.topnews.util.AppUtils
import org.koin.core.KoinComponent
import org.koin.core.inject

class TopNewsSourcesViewModel : ViewModel(), KoinComponent {

    private var mErrorObserver: Observer<Throwable>? = null
    private var mFailureObserver: Observer<FailureResponse>? = null
    private var mTopNewSourcesLiveData: RichMediatorLiveData<TopNewSourcesResponse>? = null

    // Injecting repository class
    private val mTopNewsSourcesRepo: TopNewsSourcesRepo by inject()

    //saving error & failure observers instance
    fun setGenericListeners(errorObserver: Observer<Throwable>,
        failureResponseObserver: Observer<FailureResponse>) {
        this.mErrorObserver = errorObserver
        this.mFailureObserver = failureResponseObserver
        initLiveData()
    }

    /**
     * Method is used to initialize live data objects
     */
    private fun initLiveData() {
        if (mTopNewSourcesLiveData == null) {
            mTopNewSourcesLiveData = object : RichMediatorLiveData<TopNewSourcesResponse>() {

                override val failureObserver: Observer<FailureResponse>
                    get() = mFailureObserver!!

                override val errorObserver: Observer<Throwable>
                    get() = mErrorObserver!!
            }
        }
    }

    /**
     * This method gives the top news sources list live data object to {@link TopNewSourcesFragment}
     *
     * @return {@link #mTopNewSourcesLiveData}
     */
    fun getTopNewsSourcesLiveData(): RichMediatorLiveData<TopNewSourcesResponse> {
        return mTopNewSourcesLiveData!!
    }


    /**
     * This method is used to get Top News Sources
     */
    fun getTopNewsSources() {
        mTopNewsSourcesRepo.getTopNewsSources(mTopNewSourcesLiveData!!)
    }
}