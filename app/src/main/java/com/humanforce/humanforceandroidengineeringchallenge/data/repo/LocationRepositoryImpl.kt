package com.humanforce.humanforceandroidengineeringchallenge.data.repo

import com.humanforce.humanforceandroidengineeringchallenge.BuildConfig
import com.humanforce.humanforceandroidengineeringchallenge.data.api.ApiCallHelper
import com.humanforce.humanforceandroidengineeringchallenge.data.api.ApiCallHelper.API_ERROR_MESSAGES
import com.humanforce.humanforceandroidengineeringchallenge.data.api.Resource
import com.humanforce.humanforceandroidengineeringchallenge.data.api.ApiInterface
import com.humanforce.humanforceandroidengineeringchallenge.data.db.LocationDao
import com.humanforce.humanforceandroidengineeringchallenge.data.mapper.toDailyForecastListEntities
import com.humanforce.humanforceandroidengineeringchallenge.data.mapper.toDomain
import com.humanforce.humanforceandroidengineeringchallenge.data.mapper.toLocationEntity
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.DailyForecast
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.SavedLocation
import com.humanforce.humanforceandroidengineeringchallenge.domain.repo.LocationRepository
import com.humanforce.humanforceandroidengineeringchallenge.utils.NetworkUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val api: ApiInterface,
    private val apiHelper: ApiCallHelper,
    private val locationDao: LocationDao,
    private val networkUtil: NetworkUtil
) : LocationRepository {

    override suspend fun searchLocation(cityName: String): Flow<Resource<List<SavedLocation>>> = flow {

        val locationsCacheData = locationDao.getAllSavedLocations().map { it.toDomain() }.take(5)

        if ( !networkUtil.isConnected()) {
            emit(Resource.Error(API_ERROR_MESSAGES.NETWORK_ERROR_MESSAGE
                , locationsCacheData))
        }

        emit(
            apiHelper.safeApiCall {
                val locationsApiData = api.searchCity(cityName, limit = 5, apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                val locationEntities = locationsApiData.map {
                    it.toLocationEntity()
                }
                locationDao.saveLocations(locationEntities)
                locationEntities.map { it.toDomain() }.take(5)
            }
        )
    }

}
