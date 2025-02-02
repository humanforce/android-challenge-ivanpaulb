package com.humanforce.humanforceandroidengineeringchallenge.domain.repo

import com.humanforce.humanforceandroidengineeringchallenge.data.api.Resource
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.SavedLocation

import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun searchLocation(cityName: String): Flow<Resource<List<SavedLocation>>>
}
