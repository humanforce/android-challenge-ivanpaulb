package com.humanforce.humanforceandroidengineeringchallenge.domain.models

data class DailyForecast(
    val date: String,
    val minTemp: Double,
    val maxTemp: Double,
    val condition: String,
    val icon: String
)