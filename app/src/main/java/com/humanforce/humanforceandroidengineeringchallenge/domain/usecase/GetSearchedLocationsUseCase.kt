package com.humanforce.humanforceandroidengineeringchallenge.domain.usecase

import com.humanforce.humanforceandroidengineeringchallenge.data.api.Resource
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.SavedLocation
import com.humanforce.humanforceandroidengineeringchallenge.domain.repo.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchedLocationsUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(cityName: String): Flow<Resource<List<SavedLocation>>> {
        return repository.searchLocation(cityName)
    }
}
