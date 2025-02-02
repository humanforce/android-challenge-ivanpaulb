package com.humanforce.humanforceandroidengineeringchallenge.di

import android.content.Context
import androidx.room.Room
import com.humanforce.humanforceandroidengineeringchallenge.data.api.ApiCallHelper
import com.humanforce.humanforceandroidengineeringchallenge.data.api.WeatherApi
import com.humanforce.humanforceandroidengineeringchallenge.data.db.AppDatabase
import com.humanforce.humanforceandroidengineeringchallenge.data.db.ForecastDao
import com.humanforce.humanforceandroidengineeringchallenge.data.db.WeatherDao
import com.humanforce.humanforceandroidengineeringchallenge.data.repo.WeatherRepositoryImpl
import com.humanforce.humanforceandroidengineeringchallenge.domain.repo.WeatherRepository
import com.humanforce.humanforceandroidengineeringchallenge.utils.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "weather_db").build()

    @Provides
    fun provideWeatherDao(db: AppDatabase): WeatherDao = db.weatherDao()

    @Provides
    fun provideForecastsDao(db: AppDatabase): ForecastDao = db.forecastDao()

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApi,
        apiHelper: ApiCallHelper,
        weatherDao: WeatherDao,
        forecastDao: ForecastDao,
        networkUtil: NetworkUtil
    ): WeatherRepository {
        return WeatherRepositoryImpl(api, apiHelper, weatherDao, forecastDao,
            networkUtil)
    }
}