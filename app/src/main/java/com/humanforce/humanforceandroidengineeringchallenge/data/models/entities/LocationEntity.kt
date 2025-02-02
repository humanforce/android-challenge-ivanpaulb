package com.humanforce.humanforceandroidengineeringchallenge.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations_table")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val country: String
)