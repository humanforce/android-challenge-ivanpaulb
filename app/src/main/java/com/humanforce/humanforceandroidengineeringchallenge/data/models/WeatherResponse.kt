package com.humanforce.humanforceandroidengineeringchallenge.data.models


data class WeatherResponse(
    val id: Int,
    val dt: Int,
    val name: String,
    val weather: List<WeatherDataModel>,
    val main: MainTempModel,
    val wind: WindModel,
    val visibility: Double
)