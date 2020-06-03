package com.topnews.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import com.topnews.R
import com.topnews.model.FailureResponse
import com.topnews.util.AppUtils.getUniqueDeviceId
import com.topnews.util.AppUtils.hideKeyboard
import com.topnews.util.ResourceUtils.Companion.instance

abstract class BaseActivity : AppCompatActivity() {
    private var errorObserver: Observer<Throwable>? = null
    private var failureResponseObserver: Observer<FailureResponse>? = null
    private var loadingStateObserver: Observer<Boolean>? = null
    private var baseContainer: RelativeLayout? = null
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        baseContainer = findViewById(R.id.rl_base_container)
        setLayout()
        ButterKnife.bind(this)
        initObservers()
    }

    private fun initObservers() {
        errorObserver = Observer<Throwable> { t -> onErrorOccurred(t!!) }
        failureResponseObserver = Observer<FailureResponse> { t -> onFailure(t) }
        loadingStateObserver = Observer<Boolean> { t -> onLoadingStateChanged(t) }
    }

    protected fun onLoadingStateChanged(aBoolean: Boolean) {}
    protected fun onFailure(failureResponse: FailureResponse) {
        showToastShort("failure:" + failureResponse.message)
        Log.e("onFailure: ", failureResponse.message.toString() + "   " + failureResponse.code)
    }

    protected fun onErrorOccurred(throwable: Throwable) {
        showToastShort("error")
        Log.e("onErrorOccurred: ", throwable.message)
    }

    /**
     * Method is used to set the layout in the Base Activity.
     * Layout params of the inserted child is match parent
     */
    private fun setLayout() {
        if (resourceId != -1) {
            removeLayout()
            val layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT
                , RelativeLayout.LayoutParams.MATCH_PARENT
            )
            val layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?
            if (layoutInflater != null) {
                val view = layoutInflater.inflate(resourceId, null)
                baseContainer!!.addView(view, layoutParams)
            }
        }
    }

    /**
     * hides keyboard onClick anywhere besides edit text
     *
     * @param ev
     * @return
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view: View? = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE)
            && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit."
            )
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.getLeft() - scrcoords[0]
            val y = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                (this.getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager).hideSoftInputFromWindow(
                    this.window.decorView.applicationWindowToken,
                    0
                )
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * Method used to get unique device id
     */
    val deviceId: String
        get() = getUniqueDeviceId(this)

    /**
     * Method is used by the sub class for passing the id of the layout ot be inflated in the relative layout
     *
     * @return id of the resource to be inflated
     */
    protected abstract val resourceId: Int

    fun addFragment(layoutResId: Int, fragment: BaseFragment?, tag: String?) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) supportFragmentManager.beginTransaction()
            .add(layoutResId, fragment!!, tag)
            .commit()
    }

    fun addFragmentWithBackstack(layoutResId: Int, fragment: BaseFragment?, tag: String?) {
        supportFragmentManager.beginTransaction()
            .add(layoutResId, fragment!!, tag)
            .addToBackStack(tag)
            .commit()
    }

    fun replaceFragment(layoutResId: Int, fragment: BaseFragment?, tag: String?) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) supportFragmentManager.beginTransaction()
            .replace(layoutResId, fragment!!, tag)
            .commit()
    }

    fun replaceFragmentWithBackstack(layoutResId: Int, fragment: BaseFragment?, tag: String?) {
        supportFragmentManager.beginTransaction()
            .replace(layoutResId, fragment!!, tag)
            .addToBackStack(tag)
            .commit()
    }

    fun replaceFragmentWithBackstackWithStateLoss(
        layoutResId: Int,
        fragment: BaseFragment?,
        tag: String?
    ) {
        supportFragmentManager.beginTransaction()
            .replace(layoutResId, fragment!!, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    /**
     * This method is used to remove the view already present as a child in relative layout.
     */
    private fun removeLayout() {
        if (baseContainer!!.childCount >= 1) baseContainer!!.removeAllViews()
    }

    fun showToastLong(message: CharSequence?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showToastShort(message: CharSequence?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showProgressDialog() {
        /* mProgressDialog = ProgressDialog(this, R.style.MyTheme)
         mProgressDialog!!.isIndeterminate = true
         mProgressDialog!!.setCancelable(false)
         mProgressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar_Small)
         mProgressDialog!!.show()*/
    }

    fun hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) mProgressDialog!!.dismiss()
    }

    fun showUnknownRetrofitError() {
        hideProgressDialog()
        showToastLong(instance!!.getString(R.string.something_went_wrong))
    }

    fun showNoNetworkError() {
        hideProgressDialog()
        showToastLong(instance!!.getString(R.string.no_internet))
    }

    fun hideKeyboard() {
        hideKeyboard(this)
    }

    fun popFragment() {
        if (supportFragmentManager != null) {
            supportFragmentManager.popBackStackImmediate()
        }
    }
}