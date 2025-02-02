package com.humanforce.humanforceandroidengineeringchallenge.data.api

import com.humanforce.humanforceandroidengineeringchallenge.data.models.apiresponses.CityResponse
import com.humanforce.humanforceandroidengineeringchallenge.data.models.apiresponses.ForecastResponse
import com.humanforce.humanforceandroidengineeringchallenge.data.models.apiresponses.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): WeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getFiveDayForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): ForecastResponse


    @GET("geo/1.0/direct")
    suspend fun searchCity(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String
    ): List<CityResponse>
}
