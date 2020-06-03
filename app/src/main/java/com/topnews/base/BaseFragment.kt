package com.topnews.base

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.topnews.model.FailureResponse


open class BaseFragment : androidx.fragment.app.Fragment() {

    var errorObserver: Observer<Throwable>? = null

    var failureResponseObserver: Observer<FailureResponse>? = null

    var loadingStateObserver: Observer<Boolean>? = null


    val deviceId: String
        get() = (activity as BaseActivity).deviceId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        errorObserver = Observer { throwable -> onErrorOccurred(throwable!!) }
        failureResponseObserver = Observer { failureResponse -> onFailure(failureResponse!!) }
        loadingStateObserver = Observer { aBoolean ->
            if (aBoolean != null)
                onLoadingStateChanged(aBoolean)
        }
    }

    protected fun onLoadingStateChanged(aBoolean: Boolean) {

    }

    protected fun onFailure(failureResponse: FailureResponse) {
        hideProgressDialog()
        showToastShort(failureResponse.message!!)
        Log.e("onFailure: ", failureResponse.message.toString() + "   " + failureResponse.status)
    }

    protected open fun onErrorOccurred(throwable: Throwable) {
        hideProgressDialog()
        showToastShort(throwable.message.toString())
        Log.e("onErrorOccurred: ", throwable.message)
    }

    fun showToastLong(message: CharSequence) {
        (activity as BaseActivity).showToastLong(message)
    }

    fun showToastShort(message: CharSequence) {
        (activity as BaseActivity).showToastShort(message)
    }

    fun showProgressDialog() {
        (activity as BaseActivity).showProgressDialog()
    }

    fun hideProgressDialog() {
        (activity as BaseActivity).hideProgressDialog()
    }

    fun hideKeyboard() {
        (activity as BaseActivity).hideKeyboard()
    }

    fun showNoNetworkError() {
        (activity as BaseActivity).showNoNetworkError()
    }

    fun popFragment() {
        if (fragmentManager != null) {
            fragmentManager!!.popBackStackImmediate()
        }
    }

}
