package com.topnews.ui.home.dailyfeeds

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.topnews.base.RichMediatorLiveData
import com.topnews.model.FailureResponse
import com.topnews.model.dailyfeedsresponse.DailyFeedsResponse
import org.koin.core.KoinComponent
import org.koin.core.inject

class DailyFeedsViewModel : ViewModel(), KoinComponent {

    private var mErrorObserver: Observer<Throwable>? = null

    private var mFailureObserver: Observer<FailureResponse>? = null

    private var mDailyFeedsLiveData: RichMediatorLiveData<DailyFeedsResponse>? = null

    //Injecting repository class
    private val mDailyFeedsRepo: DailyFeedsRepo by inject()

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
        if (mDailyFeedsLiveData == null) {
            mDailyFeedsLiveData = object : RichMediatorLiveData<DailyFeedsResponse>() {

                override val failureObserver: Observer<FailureResponse>
                    get() = mFailureObserver!!

                override val errorObserver: Observer<Throwable>
                    get() = mErrorObserver!!
            }
        }
    }

    /**
     * This method gives the daily feeds list live data object to {@link DailyFeedsFragment}
     *
     * @return {@link #mDailyFeedsLiveData}
     */
    fun getTopHeadlinesLiveData(): RichMediatorLiveData<DailyFeedsResponse> {
        return mDailyFeedsLiveData!!
    }

    /**
     * This method is used to get Top Headlines
     */
    fun getTopHeadlines(source: String) {
        mDailyFeedsRepo.getTopHeadlines(mDailyFeedsLiveData!!, source)
    }

}