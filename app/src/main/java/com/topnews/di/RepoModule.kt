package com.finitty.provider.di

import com.topnews.ui.home.dailyfeeds.DailyFeedsRepo
import com.topnews.ui.home.topnewsources.TopNewsSourcesRepo
import org.koin.dsl.module

val repoModule = module {

    /**
     * you can use it any KoinComponent class  below is declaration
     *  private val globalRepository: ContactRepository by inject() */

    single { TopNewsSourcesRepo() }
    single { DailyFeedsRepo() }
}