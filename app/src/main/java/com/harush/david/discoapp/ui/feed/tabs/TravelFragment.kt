package com.harush.david.discoapp.ui.feed.tabs


import com.harush.david.discoapp.ui.feed.AbstractFeedFragment
import com.harush.david.discoapp.ui.feed.FeedType
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class TravelFragment :
    AbstractFeedFragment(feedType = FeedType.TRAVEL) {
    override val updateTimeDuration: Duration = 7.seconds
    override val showProgressBar: Boolean = true
    override val recyclerSpanCount: Int = 2
}