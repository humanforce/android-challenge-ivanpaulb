package com.humanforce.humanforceandroidengineeringchallenge.di

import com.humanforce.humanforceandroidengineeringchallenge.data.api.WeatherApi
import com.humanforce.humanforceandroidengineeringchallenge.data.repo.WeatherRepositoryImpl
import com.humanforce.humanforceandroidengineeringchallenge.domain.repo.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideWeatherRepository(api: WeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }
}
