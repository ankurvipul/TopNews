package com.topnews.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.topnews.R
import com.topnews.base.BaseActivity
import com.topnews.ui.splash.topnewsources.HomeActivity
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSplashScreen()
    }

    /**
     * Open {@link HomeActivity} after a delay of 3 seconds
     */
    private fun showSplashScreen() {
        val mHandler = Handler()
        mHandler.postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }

    override val resourceId: Int
        get() = R.layout.activity_splash

    companion object {
        private const val SPLASH_TIME_OUT = 2000
    }
}
