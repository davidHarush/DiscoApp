package com.harush.david.discoapp.di


import com.harush.david.discoapp.network.IRssApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * AppModule is a Dagger module responsible for providing OkHttpClient and IRssApiService.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        private const val TIME_OUT = 5L
    }


    /**
     * Provides a singleton instance of OkHttpClient.
     * This instance is configured with a logging interceptor and a timeout.
     */
    @Provides
    @Singleton
    fun provideGlobalOkHttpClient(
    ): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
        return OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }


    /**
     * Provides a singleton instance of IRssApiService.
     * This instance is created using the OkHttpClient instance and the base URL of the RSS service.
     */
    @Provides
    @Singleton
    fun provideRssService(okHttpClient: OkHttpClient):
            IRssApiService = createWebService(
        okHttpClient = okHttpClient,
        baseUrl = IRssApiService.RSS_BASE_URL,
        converterFactory = SimpleXmlConverterFactory.create()
    )


    /**
     *   Creates a web service instance using Retrofit.
     */
    private inline fun <reified T> createWebService(
        okHttpClient: OkHttpClient,
        baseUrl: String,
        converterFactory: Converter.Factory = GsonConverterFactory.create()
    ): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
            .create(T::class.java)
    }

}