package com.humanforce.humanforceandroidengineeringchallenge.di

import android.content.Context
import com.humanforce.humanforceandroidengineeringchallenge.data.api.ApiCallHelper
import com.humanforce.humanforceandroidengineeringchallenge.data.api.WeatherApi
import com.humanforce.humanforceandroidengineeringchallenge.utils.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideApiCallHelper(): ApiCallHelper {
        return ApiCallHelper()
    }

    @Provides
    @Singleton
    fun provideNetworkUtil(@ApplicationContext context: Context): NetworkUtil = NetworkUtil(context)

}
