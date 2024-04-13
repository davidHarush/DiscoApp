package com.harush.david.discoapp.ui.feed

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.harush.david.discoapp.RssFeedState
import com.harush.david.discoapp.coroutineExceptionHandler
import com.harush.david.discoapp.databinding.FragmentFeedBinding
import com.harush.david.discoapp.gone
import com.harush.david.discoapp.ui.main.SharedDataViewModel
import com.harush.david.discoapp.ui.data_model.FeedItem
import com.harush.david.discoapp.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.time.Duration

@AndroidEntryPoint
abstract class AbstractFeedFragment(val feedType: FeedType) : Fragment() {

    private var binding: FragmentFeedBinding? = null

    // this practice will lead to NullPointerException because we call
    // fetchFeed every x seconds and the view can be destroyed until the fetchFeed is finished and the retuning of the data
    // private val binding get() = _binding!!

    private val feedViewModel: FeedViewModel by viewModels()
    private val sharedDataViewModel: SharedDataViewModel by activityViewModels()


    // The time duration to update the feed.
    abstract val updateTimeDuration: Duration

    // Boolean to control the visibility of the progress bar while refresh the feed.
    // Set to true to show the progress bar.
    abstract val showProgressBar: Boolean

    // The number of columns in the recycler view.
    abstract val recyclerSpanCount: Int


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        feedViewModel.startFetchingFeed(feedType, showProgressBar, updateTimeDuration)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding when the view is destroyed to avoid memory leaks.
        binding = null
    }


    // Open the link using customTabs.
    private fun onClick(item: FeedItem) {
        val context = context ?: return
        val webpage = Uri.parse(item.link)
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        sharedDataViewModel.setLastVisitedFeedTitle(item.title)
        customTabsIntent.launchUrl(context, webpage)
    }


    // Set the recycler view and observe the feed state.
    private fun setRecyclerView() {
        binding?.recyclerView?.apply {
            layoutManager = if (recyclerSpanCount >1){
                GridLayoutManager(context, recyclerSpanCount)
            } else {
                LinearLayoutManager(context)
            }
            adapter = FeedAdapter { item ->
                onClick(item)
            }

        }

        lifecycleScope.launch(coroutineExceptionHandler) {
            feedViewModel.rssFeedStateFlow.collect { state ->
                when (state) {
                    is RssFeedState.Success -> {
                        binding?.progressBar?.gone()
                        (binding?.recyclerView?.adapter as? FeedAdapter)?.submitList(state.feed)
                    }

                    is RssFeedState.Error -> {
                        binding?.progressBar?.gone()
                        Toast.makeText(context, state.exception.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is RssFeedState.Loading -> {
                        binding?.progressBar?.visible()
                    }
                }
            }
        }
    }
}
