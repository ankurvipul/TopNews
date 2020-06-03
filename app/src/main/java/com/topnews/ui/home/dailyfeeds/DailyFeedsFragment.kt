package com.topnews.ui.home.dailyfeeds


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.topnews.R
import com.topnews.adapter.DailyFeedsAdapter
import com.topnews.base.BaseFragment
import com.topnews.constants.AppConstants
import com.topnews.model.dailyfeedsresponse.Article
import com.topnews.model.topnewsourceresponse.Source
import com.topnews.util.AppUtils
import kotlinx.android.synthetic.main.fragment_daily_feeds.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DailyFeedsFragment : BaseFragment() {

    var sources: Source? = null
    var dailyFeedsAdapter: DailyFeedsAdapter? = null
    var article = arrayListOf<Article>()

    /**
     * A {@link HomeViewModel} object to handle all the actions and business logic
     */
    private val mDailyFeedsViewModel: DailyFeedsViewModel by viewModel()

    /**
     * A {@link IHomeHost} object to interact with the host{@link HomeActivity}
     * if any action has to be performed from the host.
     */
    private var mDailyFeedsHost: IDailyFeedsHost? = null


    /**
     * This method is used to return the instance of this fragment
     *
     * @return new instance of {@link TopNewSourcesFragment}
     */
    companion object {

        @JvmStatic
        fun getInstance(sources: Source): DailyFeedsFragment {
            val dailyFeedsFragment = DailyFeedsFragment()
            val bundle = Bundle()
            bundle.putSerializable(AppConstants.DataConstants.NEWS_SOURCES, sources)
            dailyFeedsFragment.arguments = bundle
            return dailyFeedsFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IDailyFeedsHost)
            mDailyFeedsHost = context
        else
            throw IllegalStateException("Host must implement IDailyFeedsHost")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_feeds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //getting news source from the arguments
        if (arguments != null) {
            sources = arguments!!.getSerializable(AppConstants.DataConstants.NEWS_SOURCES) as Source
            tv_title.text = getString(R.string.daily_feeds) + getString(R.string.by) + sources?.name
        }

        mDailyFeedsViewModel.setGenericListeners(errorObserver!!, failureResponseObserver!!)
        mDailyFeedsViewModel.getTopHeadlinesLiveData().observe(this, Observer {
            if (it != null) {
                //set daily feeds to list
                article.addAll(it.articles as ArrayList<Article>)
                dailyFeedsAdapter!!.notifyDataSetChanged()
                if (article.size > 0) {
                    dailyFeedsAdapter!!.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                    rv_daily_feeds.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.VISIBLE
                    rv_daily_feeds.visibility = View.GONE
                }
            }
        })
        setAdapter()
        getData()

    }

    private fun getData() {
        if (AppUtils.isInternetAvailable(context!!))
            mDailyFeedsViewModel.getTopHeadlines(sources?.id!!)
        else
            showNoNetworkError()
    }

    /**
     * Method to initialize and set DailyFeedsAdapter
     */
    private fun setAdapter() {
        dailyFeedsAdapter = DailyFeedsAdapter(context, article)
        val mLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_daily_feeds.layoutManager = mLayoutManager
        rv_daily_feeds.adapter = dailyFeedsAdapter
        dailyFeedsAdapter!!.onItemClick = { article ->
            // do something with your item
            mDailyFeedsHost?.openNewsItem(article.url)
        }
    }

    /**
     * This interface is used to interact with the host {@link HomeActivity}
     */
    interface IDailyFeedsHost {
        fun openNewsItem(url: String)
    }
}
