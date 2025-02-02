package com.humanforce.humanforceandroidengineeringchallenge.domain.models

data class Weather(
    val temperature: Double,
    val condition: String,
    val icon: String,
    val windSpeed: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val visibility: Double,
)