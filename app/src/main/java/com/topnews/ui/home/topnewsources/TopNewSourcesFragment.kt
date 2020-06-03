package com.topnews.ui.home.topnewsources


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.topnews.R
import com.topnews.adapter.TopNewSourcesAdapter
import com.topnews.base.BaseFragment
import com.topnews.model.topnewsourceresponse.Source
import com.topnews.util.AppUtils
import kotlinx.android.synthetic.main.fragment_top_new_sources.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopNewSourcesFragment : BaseFragment() {

    var topNewSourcesAdapter: TopNewSourcesAdapter? = null
    var topNewSources = arrayListOf<Source>()

    /**
     * A {@link TopNewsSourcesViewModel} object to handle all the actions and business logic
     */
    private val mTopNewsSourcesViewModel: TopNewsSourcesViewModel by viewModel()

    /**
     * A {@link ITopNewSourcesHost} object to interact with the host{@link HomeActivity}
     * if any action has to be performed from the host.
     */
    private var mTopNewSourcesHost: ITopNewSourcesHost? = null

    /**
     * This method is used to return the instance of this fragment
     *
     * @return new instance of {@link TopNewSourcesFragment}
     */
    companion object {

        @JvmStatic
        fun getInstance(): TopNewSourcesFragment {
            return TopNewSourcesFragment()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ITopNewSourcesHost)
            mTopNewSourcesHost = context
        else
            throw IllegalStateException("Host must implement IHomeHost")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_new_sources, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // mTopNewsSourcesViewModel = ViewModelProviders.of(this).get(TopNewsSourcesViewModel::class.java)
        mTopNewsSourcesViewModel.setGenericListeners(errorObserver!!, failureResponseObserver!!)
        mTopNewsSourcesViewModel.getTopNewsSourcesLiveData().observe(this, Observer {
            if (it != null) {
                //set top news sources to list
                topNewSources.addAll(it.sources as ArrayList<Source>)
                if (topNewSources.size > 0) {
                    topNewSourcesAdapter!!.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                    rv_top_new_sources.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.VISIBLE
                    rv_top_new_sources.visibility = View.GONE
                }
            }
        })
        setAdapter()
        getData()

    }

    private fun getData() {
        if (AppUtils.isInternetAvailable(context!!))
            mTopNewsSourcesViewModel.getTopNewsSources()
        else
            showNoNetworkError()
    }


    /**
     * Method to initialize and set TopNewSourcesAdapter
     */
    private fun setAdapter() {
        topNewSourcesAdapter = TopNewSourcesAdapter(context, topNewSources)
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        rv_top_new_sources.layoutManager = mLayoutManager
        rv_top_new_sources.adapter = topNewSourcesAdapter
        topNewSourcesAdapter?.onItemClick = { sources ->
            // do something with your item
            Log.e("TAG", sources.name!!)
            mTopNewSourcesHost!!.openDailyFeeds(sources)
        }
    }

    /**
     * This interface is used to interact with the host {@link HomeActivity}
     */
    interface ITopNewSourcesHost {
        fun openDailyFeeds(source: Source)
    }
}
