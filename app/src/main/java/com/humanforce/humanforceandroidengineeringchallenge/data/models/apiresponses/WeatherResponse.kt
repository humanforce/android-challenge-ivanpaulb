package com.humanforce.humanforceandroidengineeringchallenge.data.models.apiresponses

import com.humanforce.humanforceandroidengineeringchallenge.data.models.MainTempModel
import com.humanforce.humanforceandroidengineeringchallenge.data.models.WeatherDataModel
import com.humanforce.humanforceandroidengineeringchallenge.data.models.WindModel


data class WeatherResponse(
    val id: Int,
    val dt: Int,
    val name: String,
    val weather: List<WeatherDataModel>,
    val main: MainTempModel,
    val wind: WindModel,
    val visibility: Double
)