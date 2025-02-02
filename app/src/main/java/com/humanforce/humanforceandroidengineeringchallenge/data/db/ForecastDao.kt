package com.humanforce.humanforceandroidengineeringchallenge.data.db


import androidx.room.*
import com.humanforce.humanforceandroidengineeringchallenge.data.models.entities.ForecastEntity
import com.humanforce.humanforceandroidengineeringchallenge.data.models.entities.WeatherEntity


@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateForecasts(forecasts: List<ForecastEntity>)


    suspend fun insertAndReplaceForecasts(forecasts: List<ForecastEntity>) {
        clearAllForecasts()
        insertOrUpdateForecasts(forecasts)
    }
    @Query("SELECT * FROM forecast_table ORDER BY date ASC")
    fun getAllForecasts(): List<ForecastEntity>

    @Delete
    suspend fun deleteForecast(forecast: ForecastEntity)

    @Query("DELETE FROM forecast_table")
    suspend fun clearAllForecasts()

}