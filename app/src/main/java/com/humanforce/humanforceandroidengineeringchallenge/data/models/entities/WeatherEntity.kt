package com.humanforce.humanforceandroidengineeringchallenge.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val temperature: Double,
    val condition: String,
    val icon: String,
    val windSpeed: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val visibility: Double
)

