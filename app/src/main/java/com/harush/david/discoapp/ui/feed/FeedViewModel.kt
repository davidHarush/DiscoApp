package com.harush.david.discoapp.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harush.david.discoapp.RssFeedState
import com.harush.david.discoapp.coroutineExceptionHandler
import com.harush.david.discoapp.network.RssChannel
import com.harush.david.discoapp.network.RssFeed
import com.harush.david.discoapp.repo.FeedRepo
import com.harush.david.discoapp.ui.model.FeedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration


@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repo: FeedRepo
) : ViewModel() {

    private val _rssFeedStateFlow = MutableStateFlow<RssFeedState>(RssFeedState.Loading)
    val rssFeedStateFlow: StateFlow<RssFeedState> = _rssFeedStateFlow.asStateFlow()

    fun startFetchingFeed(feedType: FeedType, showProgressBar: Boolean, updateTimeDuration: Duration) {
        viewModelScope.launch(coroutineExceptionHandler) {
            while (isActive) {  // Ensures the coroutine runs only until the lifecycle finishes
                fetchFeed(feedType = feedType, showProgressBar = showProgressBar)
                delay(updateTimeDuration)
            }
        }
    }

   private fun fetchFeed(feedType: FeedType, showProgressBar: Boolean) {
        if (showProgressBar) _rssFeedStateFlow.value = RssFeedState.Loading

        viewModelScope.launch(coroutineExceptionHandler) {
            when (feedType) {
                FeedType.TRAVEL -> fetchTravelRSSFeed()
                FeedType.WORLD_SPORT_AND_ENTERTAINMENT -> fetchSportAndEntertainmentRSSFeed()
            }
        }
    }

    private suspend fun fetchTravelRSSFeed() {
        repo.fetchTravelNews()
            .catch { exception -> _rssFeedStateFlow.value = RssFeedState.Error(exception) }
            .collect { rssFeed -> _rssFeedStateFlow.value = RssFeedState.Success(convertToFeedItems(rssFeed) ) }
    }

    private fun convertToFeedItems(rssFeed: RssFeed): List<FeedItem> {
        return rssFeed.channel?.items?.map { rssItem ->
            FeedItem(
                title = rssItem.title,
                description = rssItem.description.orEmpty(),
                link = rssItem.link.orEmpty()
            )
        }.orEmpty()
    }

    private suspend fun fetchSportAndEntertainmentRSSFeed() {
        coroutineScope {
            val sport = async {
                repo.fetchSportNews()
                    .catch { exception ->
                        _rssFeedStateFlow.value = RssFeedState.Error(exception)
                    }
                    .first()
            }
            val entertainment = async {
                repo.fetchEntertainmentNews()
                    .catch { exception ->
                        _rssFeedStateFlow.value = RssFeedState.Error(exception)
                    }
                    .first()
            }

            val sportItems = sport.await().channel?.items.orEmpty()
            val entertainmentItems = entertainment.await().channel?.items.orEmpty()

            val combinedItems = sportItems + entertainmentItems
            // convert to FeedItem list
            _rssFeedStateFlow.value = if (combinedItems.isEmpty()) {
                RssFeedState.Error(Exception("Failed to fetch data"))
            } else {
                RssFeedState.Success(combinedItems.map { rssItem ->
                    FeedItem(
                        title = rssItem.title,
                        description = rssItem.description.orEmpty(),
                        link = rssItem.link
                    )
                })
            }
        }
    }
}
