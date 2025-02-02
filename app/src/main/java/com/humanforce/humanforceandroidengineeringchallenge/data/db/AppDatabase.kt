package com.humanforce.humanforceandroidengineeringchallenge.data.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.humanforce.humanforceandroidengineeringchallenge.data.models.entities.ForecastEntity
import com.humanforce.humanforceandroidengineeringchallenge.data.models.entities.LocationEntity
import com.humanforce.humanforceandroidengineeringchallenge.data.models.entities.WeatherEntity


@Database(
    entities = [
        WeatherEntity::class,
        ForecastEntity::class,
        LocationEntity:: class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun forecastDao(): ForecastDao
    abstract fun locationDao(): LocationDao
}
