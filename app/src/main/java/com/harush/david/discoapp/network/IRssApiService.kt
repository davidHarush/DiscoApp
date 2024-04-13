package com.harush.david.discoapp.network

import retrofit2.http.GET


interface IRssApiService {
    companion object {
        const val RSS_BASE_URL = "http://rss.cnn.com/rss/"
    }

    @GET("edition_travel.rss")
    suspend fun fetchTravelNews(): RssFeed

    @GET("edition_sport.rss")
    suspend fun fetchSportNews(): RssFeed

    @GET("edition_entertainment.rss")
    suspend fun fetchEntertainmentNews(): RssFeed
}