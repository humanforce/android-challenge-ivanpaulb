package com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.humanforce.humanforceandroidengineeringchallenge.data.api.Resource
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.SavedLocation
import com.humanforce.humanforceandroidengineeringchallenge.domain.repo.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val repository: LocationRepository
) : ViewModel() {


    private val _locationState = MutableStateFlow<LocationState>(LocationState.NONE)
    val locationState = _locationState.asStateFlow()


    private val _currentLocationState = MutableStateFlow<LocationState>(LocationState.NONE)

    private val _searchResults = MutableStateFlow<Resource<List<SavedLocation>>>(Resource.Loading)
    val searchResults: StateFlow<Resource<List<SavedLocation>>> = _searchResults


    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        _currentLocationState.value = LocationState.Success(location.latitude, location.longitude)
                        _locationState.value = LocationState.Success(location.latitude, location.longitude)
                    } else {

                        _locationState.value = LocationState.Error("Location not found.")
                    }
                }.addOnFailureListener { exception ->
                    _locationState.value = LocationState.Error("Error getting location: ${exception.message}")
                }
            } catch (e: Exception) {
                _locationState.value =
                    LocationState.Error("Error requesting location: ${e.message}")
            }
        }

    }


    fun retrieveCurrentLocationState(){
        _locationState.value = _currentLocationState.value
    }

    fun updateLocationState(locState: LocationState) {
        _locationState.value = locState
    }


    fun searchLocation(cityName: String) {
        viewModelScope.launch(Dispatchers.IO)  {
            repository.searchLocation(cityName).collectLatest { result ->
                _searchResults.value = result
            }
        }
    }
}

sealed class LocationState {
    object NONE : LocationState()
    data class Success(val latitude: Double, val longitude: Double) : LocationState()
    data class Error(val errorMessage: String) : LocationState()
    object LocationServicesDisabled : LocationState()
    object PermissionDenied : LocationState()
}