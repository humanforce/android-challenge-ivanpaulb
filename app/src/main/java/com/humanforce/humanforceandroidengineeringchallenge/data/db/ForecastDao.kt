package com.humanforce.humanforceandroidengineeringchallenge.data.db


import androidx.room.*
import com.humanforce.humanforceandroidengineeringchallenge.data.models.entities.ForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateForecasts(forecasts: List<ForecastEntity>)

    @Query("SELECT * FROM forecast_table ORDER BY date ASC")
    fun getAllForecasts(): List<ForecastEntity>

    @Delete
    suspend fun deleteForecast(forecast: ForecastEntity)

    @Query("DELETE FROM forecast_table")
    suspend fun clearAllForecasts()

}