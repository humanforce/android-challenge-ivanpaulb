package com.humanforce.humanforceandroidengineeringchallenge

import android.Manifest
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.humanforce.humanforceandroidengineeringchallenge.presentation.ui.LocationPermissionNotice
import com.humanforce.humanforceandroidengineeringchallenge.presentation.ui.WeatherDashboard
import com.humanforce.humanforceandroidengineeringchallenge.presentation.ui.theme.AppTheme
import com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel.LocationState
import com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel.LocationViewModel
import com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel.WeatherViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    locationViewModel.getCurrentLocation()
                } else {
                    locationViewModel.updateLocationState(LocationState.PermissionDenied)
                }
            }


        setContent {
            val locationState by locationViewModel.locationState.collectAsState()
            var showEnableLocationModal by remember { mutableStateOf(false) }
            var showGrantLocationModal by remember { mutableStateOf(false) }

            val fallbackScenarioModifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(20.dp)

            AppTheme {
                WeatherDashboard(weatherViewModel, locationViewModel)

                LaunchedEffect(Unit) {
                    val locationManager = getSystemService(LocationManager::class.java)
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        locationViewModel.updateLocationState(LocationState.LocationServicesDisabled)
                        showEnableLocationModal = true
                    } else {
                        if (ContextCompat.checkSelfPermission(
                                this@MainActivity,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                        ) {
                            locationViewModel.getCurrentLocation()
                        } else {
                            showGrantLocationModal = true
                        }
                    }
                }

                when (locationState) {
                    is LocationState.Success -> {
                        Log.d("MainActivity", "Location Retrieval Success")
                    }

                    is LocationState.Error -> {
                        Toast.makeText(
                            LocalContext.current,
                            (locationState as LocationState.Error).errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is LocationState.LocationServicesDisabled -> {
                        LocationPermissionNotice(
                            modifier = fallbackScenarioModifier,
                            btnLabel= stringResource(R.string.enable_location_label),
                            showDialog = showEnableLocationModal,
                            onDismissRequest = {
                                showEnableLocationModal = false
                            }){
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(intent)
                        }
                    }

                    is LocationState.PermissionDenied -> {
                        LocationPermissionNotice(
                            modifier = fallbackScenarioModifier,
                            btnLabel= stringResource(R.string.grant_location_label),
                            showDialog = showGrantLocationModal,
                            onDismissRequest = {
                                showGrantLocationModal = false
                            }){
                            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

                        }
                    } else -> {
                        LocationPermissionNotice(
                            modifier = fallbackScenarioModifier,
                            btnLabel= stringResource(R.string.grant_location_label),
                            showDialog = showGrantLocationModal,
                            onDismissRequest = {
                                showGrantLocationModal = false
                            }){
                            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

                        }
                    }
                }
            }
        }

    }


}
