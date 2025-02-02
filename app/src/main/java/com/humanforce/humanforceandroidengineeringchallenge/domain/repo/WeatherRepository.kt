package com.humanforce.humanforceandroidengineeringchallenge.domain.repo

import com.humanforce.humanforceandroidengineeringchallenge.data.api.Resource
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.DailyForecast
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.Weather

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Resource<Weather>
    suspend fun getFiveDayForecast(lat: Double, lon: Double): Resource<List<DailyForecast>>
}
