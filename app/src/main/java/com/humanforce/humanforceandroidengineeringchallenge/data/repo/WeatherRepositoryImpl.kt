package com.humanforce.humanforceandroidengineeringchallenge.data.repo

import com.humanforce.humanforceandroidengineeringchallenge.BuildConfig
import com.humanforce.humanforceandroidengineeringchallenge.data.api.ApiCallHelper
import com.humanforce.humanforceandroidengineeringchallenge.data.api.ApiCallHelper.API_ERROR_MESSAGES
import com.humanforce.humanforceandroidengineeringchallenge.data.api.Resource
import com.humanforce.humanforceandroidengineeringchallenge.data.api.ApiInterface
import com.humanforce.humanforceandroidengineeringchallenge.data.db.ForecastDao
import com.humanforce.humanforceandroidengineeringchallenge.data.db.WeatherDao
import com.humanforce.humanforceandroidengineeringchallenge.data.mapper.toDailyForecastListEntities
import com.humanforce.humanforceandroidengineeringchallenge.data.mapper.toDomain
import com.humanforce.humanforceandroidengineeringchallenge.data.mapper.toWeatherEntity
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.DailyForecast
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.Weather
import com.humanforce.humanforceandroidengineeringchallenge.domain.repo.WeatherRepository
import com.humanforce.humanforceandroidengineeringchallenge.utils.NetworkUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val api: ApiInterface,
    private val apiHelper: ApiCallHelper,
    private val weatherDao: WeatherDao,
    private val forecastDao: ForecastDao,
    private val networkUtil: NetworkUtil
) : WeatherRepository {
    override suspend fun getCurrentWeather(lat: Double, lon: Double): Resource<Weather> {

        val weatherCacheData = weatherDao.getWeather()?.toDomain()

        if (!networkUtil.isConnected()) {
            return Resource.Error(API_ERROR_MESSAGES.NETWORK_ERROR_MESSAGE
                , weatherCacheData)
        }

        return apiHelper.safeApiCall {
            val weatherApiData = api.getCurrentWeather(lat, lon, apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY)

            val weatherEntity = weatherApiData.toWeatherEntity()
            weatherDao.insertAndReplaceWeather(weatherEntity)
            weatherEntity.toDomain()
        }
    }

    override suspend fun getFiveDayForecast(lat: Double, lon: Double)
            : Resource<List<DailyForecast>> {

        val forecastsCacheData = forecastDao.getAllForecasts().map { it.toDomain() }

        if (!networkUtil.isConnected()) {
            return Resource.Error(API_ERROR_MESSAGES.NETWORK_ERROR_MESSAGE
                , forecastsCacheData)
        }

        return apiHelper.safeApiCall {
            val forecastsApiData =
                api.getFiveDayForecast(lat, lon, apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY)
            val forecastEntities = forecastsApiData.toDailyForecastListEntities()
            forecastDao.insertAndReplaceForecasts(forecastEntities)
            forecastEntities.map { it.toDomain() }
        }
    }
}
