package com.topnews.base

import android.util.Log
import com.google.gson.Gson
import com.topnews.model.FailureResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class NetworkCallback<T> : Callback<T> {

    private val failureResponseForNoNetwork: FailureResponse
        get() {
            val failureResponse = FailureResponse()
            failureResponse.message = "No Network"
            failureResponse.status = NO_INTERNET
            return failureResponse
        }

    abstract fun onSuccess(t: T?)

    abstract fun onFailure(failureResponse: FailureResponse)

    abstract fun onError(t: Throwable)

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            Log.e("API_Success", " : " + Gson().toJson(response.body()))
            onSuccess(response.body())
        } else {
            val failureErrorBody = getFailureErrorBody(response)
            onFailure(failureErrorBody)
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        if (t is SocketTimeoutException || t is UnknownHostException) {
            val failureResponseForNoNetwork = failureResponseForNoNetwork
            onFailure(failureResponseForNoNetwork)
        } else {
            onError(t)
        }
    }

    /**
     * Create your custom failure response out of server response
     * Also save Url for any further use
     */
    private fun getFailureErrorBody(errorBody: Response<T>): FailureResponse {
        val failureResponse = FailureResponse()
        failureResponse.status = errorBody.code()
        failureResponse.message = errorBody.message()
        return failureResponse
    }

    companion object {

        val AUTH_FAILED = 99
        val NO_INTERNET = 9
    }
}
