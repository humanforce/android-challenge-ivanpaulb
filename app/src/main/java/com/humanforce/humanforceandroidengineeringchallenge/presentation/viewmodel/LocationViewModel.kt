package com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModel() {


    private val _locationState = MutableStateFlow<LocationState>(LocationState.Loading)
    val locationState = _locationState.asStateFlow()

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO){

            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        _locationState.value = LocationState.Success(location.latitude, location.longitude)
                    } else {
                        _locationState.value = LocationState.Error("Location not found.")
                    }
                }.addOnFailureListener { exception ->
                    _locationState.value = LocationState.Error("Error getting location: ${exception.message}")
                }
            } catch (e: Exception) {
                _locationState.value = LocationState.Error("Error requesting location: ${e.message}")
            }
        }
    }


    fun updateLocationState(locState: LocationState) {
        _locationState.value = locState
    }
}

sealed class LocationState {
    object Loading : LocationState()
    data class Success(val latitude: Double, val longitude: Double) : LocationState()
    data class Error(val errorMessage: String) : LocationState()
    object LocationServicesDisabled : LocationState()
    object PermissionDenied : LocationState()
}