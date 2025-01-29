package com.humanforce.humanforceandroidengineeringchallenge.data.models

data class WeatherForecastModel(
    val dt: Int,
    val dt_txt: String,
    val weather: List<WeatherDataModel>,
    val main: MainTempModel,
    val wind: WindModel
)