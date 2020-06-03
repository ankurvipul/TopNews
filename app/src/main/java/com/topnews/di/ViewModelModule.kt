package com.finitty.provider.di

import com.topnews.ui.home.dailyfeeds.DailyFeedsViewModel
import com.topnews.ui.home.topnewsources.TopNewsSourcesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {

    /**Provide ViewModel object in activity Class
     * you can use it any Activity/Fragment class  below is declaration
     *  */

    viewModel { TopNewsSourcesViewModel() }
    viewModel { DailyFeedsViewModel() }
}