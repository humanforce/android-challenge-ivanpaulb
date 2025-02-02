package com.humanforce.humanforceandroidengineeringchallenge.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_table")
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // Keep date as String for simplicity
    val minTemp: Double,
    val maxTemp: Double,
    val condition: String,
    val icon: String
)