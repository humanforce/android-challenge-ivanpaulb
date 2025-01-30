package com.humanforce.humanforceandroidengineeringchallenge.data.repo

import com.humanforce.humanforceandroidengineeringchallenge.BuildConfig
import com.humanforce.humanforceandroidengineeringchallenge.data.api.ApiCallHelper
import com.humanforce.humanforceandroidengineeringchallenge.data.api.Resource
import com.humanforce.humanforceandroidengineeringchallenge.data.api.WeatherApi
import com.humanforce.humanforceandroidengineeringchallenge.data.mapper.toDailyForecastListDomainModel
import com.humanforce.humanforceandroidengineeringchallenge.data.mapper.toWeatherDomainModel
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.DailyForecast
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.Weather
import com.humanforce.humanforceandroidengineeringchallenge.domain.repo.WeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val apiCallHelper: ApiCallHelper
) : WeatherRepository {
    override suspend fun getCurrentWeather(lat: Double, lon: Double): Resource<Weather> {
        return apiCallHelper.safeApiCall {
            api.getCurrentWeather(lat, lon, apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .toWeatherDomainModel()
        }
    }

    override suspend fun getFiveDayForecast(lat: Double, lon: Double): Resource<List<DailyForecast>> {
        return apiCallHelper.safeApiCall {
            api.getFiveDayForecast(lat, lon, apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .toDailyForecastListDomainModel()
        }
    }
}
