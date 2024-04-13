package com.harush.david.discoapp.ui.feed.tabs


import com.harush.david.discoapp.ui.feed.AbstractFeedFragment
import com.harush.david.discoapp.ui.feed.FeedType
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class SportFragment :
    AbstractFeedFragment(feedType = FeedType.WORLD_SPORT_AND_ENTERTAINMENT) {
    override val updateTimeDuration: Duration = 6.seconds
    override val showProgressBar: Boolean = true
    override val recyclerSpanCount: Int = 1
}