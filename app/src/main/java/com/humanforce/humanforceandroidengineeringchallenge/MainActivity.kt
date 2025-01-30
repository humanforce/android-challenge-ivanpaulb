package com.humanforce.humanforceandroidengineeringchallenge

import android.Manifest
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.humanforce.humanforceandroidengineeringchallenge.presentation.ui.WeatherDashboard
import com.humanforce.humanforceandroidengineeringchallenge.presentation.ui.theme.AppTheme
import com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel.LocationState
import com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel.LocationViewModel
import com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
                }
            }

        setContent {
            val locationState by locationViewModel.locationState.collectAsState()

            LaunchedEffect(Unit) {
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PermissionChecker.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                } else {
                    locationViewModel.getCurrentLocation()
                }
            }

            val fallbackScenarioModifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(vertical = 50.dp)

            AppTheme {
                when (locationState) {
                    is LocationState.Loading -> {
                        Column(modifier = fallbackScenarioModifier) {
                            CircularProgressIndicator()
                        }
                    }
                    is LocationState.Success -> {
                        WeatherDashboard(weatherViewModel, locationViewModel)
                    }
                    is LocationState.Error -> {
                        val context = LocalContext.current
                        Toast.makeText(context, stringResource(R.string.generic_error),
                            Toast.LENGTH_SHORT).show()
                    }
                    is LocationState.LocationServicesDisabled -> {
                        Column(modifier = fallbackScenarioModifier) {
                            Button(onClick = {
                                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                startActivity(intent)
                            }) {
                                Text("Enable Location Services")
                            }
                        }
                    }
                    is LocationState.PermissionDenied -> {

                        Column(modifier = fallbackScenarioModifier) {
                            Button(onClick = {
                                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }) {
                                Text("Grant Location Permission")
                            }
                        }
                    }
                }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkLocationServicesEnabled()
            }
        }
    }

    private fun checkLocationServicesEnabled() {
        val locationManager = getSystemService(LocationManager::class.java)
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationViewModel.updateLocationState(LocationState.LocationServicesDisabled)
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                locationViewModel.getCurrentLocation()
            } else {
                locationViewModel.updateLocationState(LocationState.PermissionDenied)
            }
        }
    }
}
