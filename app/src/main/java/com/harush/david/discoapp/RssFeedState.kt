package com.harush.david.discoapp

import com.harush.david.discoapp.ui.data_model.FeedItem


/**
 * Sealed class representing the state of the RssFeed.
 */
sealed class RssFeedState {
    data object Loading : RssFeedState()
    data class Success(val feed: List<FeedItem>) : RssFeedState()
    data class Error(val exception: Throwable) : RssFeedState()
}

