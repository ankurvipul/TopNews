package com.topnews.ui.splash.topnewsources

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.topnews.R
import com.topnews.base.BaseActivity
import com.topnews.model.topnewsourceresponse.Source
import com.topnews.ui.home.dailyfeeds.DailyFeedsFragment
import com.topnews.ui.home.topnewsources.TopNewSourcesFragment


class HomeActivity : BaseActivity(), TopNewSourcesFragment.ITopNewSourcesHost,
    DailyFeedsFragment.IDailyFeedsHost {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTopNewSourcesFragment()
    }

    /**
     * Open {@link TopNewSourcesFragment} whenever {@link HomeActivity} is created
     */
    private fun addTopNewSourcesFragment() {
        addFragment(R.id.home_container, TopNewSourcesFragment.getInstance(),
            TopNewSourcesFragment::class.java.simpleName)
    }

    override val resourceId: Int
        get() = R.layout.activity_home

    /**
     * Open {@link DailyFeedsFragment} when user click a source from list
     */
    override fun openDailyFeeds(source: Source) {
        addFragmentWithBackstack(R.id.home_container, DailyFeedsFragment.getInstance(source),
            DailyFeedsFragment::class.java.simpleName)
    }

    /**
     * Send url via intent to brower, when user click on news item
     */
    override fun openNewsItem(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
