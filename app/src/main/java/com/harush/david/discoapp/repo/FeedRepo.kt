package com.harush.david.discoapp.repo

import com.harush.david.discoapp.network.IRssApiService
import com.harush.david.discoapp.network.RssFeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepo @Inject constructor(
    private val rssService: IRssApiService
) {

    fun fetchTravelNews(): Flow<RssFeed> = flow {
        val travelNews = rssService.fetchTravelNews()
        emit(travelNews)
    }

    fun fetchEntertainmentNews(): Flow<RssFeed> = flow {
        val travelNews = rssService.fetchEntertainmentNews()
        emit(travelNews)
    }

    fun fetchSportNews(): Flow<RssFeed> = flow {
        val travelNews = rssService.fetchSportNews()
        emit(travelNews)
    }

}