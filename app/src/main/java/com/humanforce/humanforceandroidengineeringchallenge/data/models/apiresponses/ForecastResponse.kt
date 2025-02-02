package com.humanforce.humanforceandroidengineeringchallenge.data.models.apiresponses

import com.humanforce.humanforceandroidengineeringchallenge.data.models.WeatherForecastModel

data class ForecastResponse(
    val list: List<WeatherForecastModel>
)