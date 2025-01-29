package com.humanforce.humanforceandroidengineeringchallenge.domain.repo

import com.humanforce.humanforceandroidengineeringchallenge.domain.models.DailyForecast
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.Weather

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Weather
    suspend fun getFiveDayForecast(lat: Double, lon: Double): List<DailyForecast>
}
