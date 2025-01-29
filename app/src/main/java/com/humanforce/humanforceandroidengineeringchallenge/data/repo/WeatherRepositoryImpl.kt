package com.humanforce.humanforceandroidengineeringchallenge.data.repo

import android.util.Log
import com.humanforce.humanforceandroidengineeringchallenge.BuildConfig
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
    private val api: WeatherApi
) : WeatherRepository {
    override suspend fun getCurrentWeather(lat: Double, lon: Double): Weather {
        val response = api.getCurrentWeather(lat, lon, apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        return response.toWeatherDomainModel()
    }

    override suspend fun getFiveDayForecast(lat: Double, lon: Double): List<DailyForecast> {
        val response = api.getFiveDayForecast(lat, lon, apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        return response.toDailyForecastListDomainModel()
    }
}
