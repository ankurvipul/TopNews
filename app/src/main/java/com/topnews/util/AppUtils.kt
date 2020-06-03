package com.topnews.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.pm.PackageInfoCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.topnews.R
import java.text.SimpleDateFormat
import java.text.ParseException as ParseException1


object AppUtils {

    /**
     * Method to hide keyboard
     */
    fun hideKeyboard(context: Activity) {
        try { // use application level context to avoid unnecessary leaks.
            val inputManager =
                context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                context.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Method to get show keyboard
     */
    fun showKeyboard(context: Activity) {
        try {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(context.currentFocus, InputMethodManager.SHOW_IMPLICIT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Method to get unique device Id
     */
    @SuppressLint("HardwareIds")
    fun getUniqueDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * Gets the version name of the application. For e.g. 1.9.3
     */
    fun getApplicationVersionNumber(context: Context?): String? {
        var versionName: String? = null
        if (context == null) {
            return versionName
        }
        try {
            versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionName
    }

    /**
     * Gets the version code of the application. For e.g. Maverick Meerkat or 2013050301
     */
    fun getApplicationVersionCode(ctx: Context): Int {
        try {
            val pInfo: PackageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0)
            val longVersionCode = PackageInfoCompat.getLongVersionCode(pInfo)
            return longVersionCode.toInt()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * Method to load image with cache using glide
     */
    fun loadImage(context: Context?, url: String?, imageView: ImageView) {
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        Glide.with(context!!).load(url).apply(requestOptions)
            .placeholder(R.drawable.news_placeholder).into(imageView)
    }

    /**
     * Method to check string is null or not
     */
    fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && !str.isEmpty())
            return false
        return true
    }

    /**
     * Method to convert date
     */
    fun convertDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormat = SimpleDateFormat("MMM, dd yyyy")
        try {
            val date = inputFormat.parse(date)

            return outputFormat.format(date)
        } catch (e: ParseException1) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * Method to internet
     */
    open fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    /**
     * Gets the version number of the Android OS For e.g. 2.3.4 or 4.1.2
     */
    val osVersion: String
        get() = Build.VERSION.RELEASE
}