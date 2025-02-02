package com.humanforce.humanforceandroidengineeringchallenge.di

import android.content.Context
import androidx.room.Room
import com.humanforce.humanforceandroidengineeringchallenge.data.api.ApiCallHelper
import com.humanforce.humanforceandroidengineeringchallenge.data.api.ApiInterface
import com.humanforce.humanforceandroidengineeringchallenge.data.db.AppDatabase
import com.humanforce.humanforceandroidengineeringchallenge.data.db.ForecastDao
import com.humanforce.humanforceandroidengineeringchallenge.data.db.LocationDao
import com.humanforce.humanforceandroidengineeringchallenge.data.db.WeatherDao
import com.humanforce.humanforceandroidengineeringchallenge.data.repo.LocationRepositoryImpl
import com.humanforce.humanforceandroidengineeringchallenge.data.repo.WeatherRepositoryImpl
import com.humanforce.humanforceandroidengineeringchallenge.domain.repo.LocationRepository
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
    fun provideLocationDao(db: AppDatabase): LocationDao = db.locationDao()

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: ApiInterface,
        apiHelper: ApiCallHelper,
        weatherDao: WeatherDao,
        forecastDao: ForecastDao,
        networkUtil: NetworkUtil
    ): WeatherRepository {
        return WeatherRepositoryImpl(api, apiHelper, weatherDao, forecastDao,
            networkUtil)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        api: ApiInterface,
        apiHelper: ApiCallHelper,
        locationDao: LocationDao,
        networkUtil: NetworkUtil
    ): LocationRepository {
        return LocationRepositoryImpl(api, apiHelper, locationDao,
            networkUtil)
    }
}