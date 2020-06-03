package com.topnews

import android.app.Application
import android.content.Context
import com.finitty.provider.di.repoModule
import com.finitty.provider.di.viewModelModule
import com.topnews.data.DataManager
import com.topnews.util.ResourceUtils
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

@ReportsCrashes(
    mailTo = "parasv49@gmail.com",
    mode = ReportingInteractionMode.TOAST,
    resToastText = R.string.crash_toast_text
)
class TopNewsApplication : Application() {

    var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        /* initialize resources utile class*/
        ResourceUtils.init(this)

        /* initialize Data Manager*/
        val dataManager = DataManager.init(context!!)
        dataManager!!.initApiManager()

        /*** start Koin DI  */
        startKoin {
            androidLogger()
            androidContext(this@TopNewsApplication)
            modules(getModule())
        }
    }

    /*** function to get all di modules array*/
    private fun getModule(): List<Module> {
        return listOf(repoModule, viewModelModule)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        System.gc()
    }
}

