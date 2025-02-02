package com.humanforce.humanforceandroidengineeringchallenge.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.humanforce.humanforceandroidengineeringchallenge.data.models.entities.LocationEntity

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations_table")
    fun getAllSavedLocations(): List<LocationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocation(location: LocationEntity)

    @Delete
    suspend fun deleteLocation(location: LocationEntity)

    @Query("DELETE FROM locations_table")
    suspend fun clearLocation()

    suspend fun clearAndSaveLocations(locations: List<LocationEntity>) {
        clearLocation()
        saveLocations(locations)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocations(entities: List<LocationEntity>)
}
