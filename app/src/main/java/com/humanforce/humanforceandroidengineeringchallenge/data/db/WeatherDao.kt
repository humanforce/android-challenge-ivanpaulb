package com.humanforce.humanforceandroidengineeringchallenge.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.humanforce.humanforceandroidengineeringchallenge.data.models.entities.WeatherEntity


@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_table LIMIT 1")
    fun getWeather(): WeatherEntity

    suspend fun insertAndReplaceWeather(weather: WeatherEntity) {
        clearWeather()
        insertOrUpdateWeather(weather)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWeather(weather: WeatherEntity)

    @Query("DELETE FROM weather_table")
    suspend fun clearWeather()


}