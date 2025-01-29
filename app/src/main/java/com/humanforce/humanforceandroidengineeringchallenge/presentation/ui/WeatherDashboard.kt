package com.humanforce.humanforceandroidengineeringchallenge.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.location.Location
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel.LocationState
import com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel.LocationViewModel
import com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel.WeatherUiState
import com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel.WeatherViewModel

@Composable
fun WeatherDashboard(weatherViewModel: WeatherViewModel = hiltViewModel(),
                     locationViewModel: LocationViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val uiState by weatherViewModel.uiState.collectAsState()
    val locationState by locationViewModel.locationState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(10.dp)) {
        TextField(
            value = weatherViewModel.searchQuery.value,
            onValueChange = { query ->
                weatherViewModel.searchQuery(query)
            },
            label = { Text("Search city") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )


        when (uiState) {
            is WeatherUiState.Loading -> CircularProgressIndicator(context)
            is WeatherUiState.Success -> {
                val weather = (uiState as WeatherUiState.Success).weather
                val forecast = (uiState as WeatherUiState.Success).forecast
                WeatherWidget(
                    weatherDetails = weather,
                    modifier = Modifier.fillMaxWidth())

                ForecastWidget(
                    forecastDetails = forecast,
                    modifier = Modifier.fillMaxWidth())
            }
            is WeatherUiState.Error -> {
                Text("Error: ${(uiState as WeatherUiState.Error).message}")
            }
        }

        LaunchedEffect(locationState) {
            locationState.let {
                val currentLocation = (locationState as LocationState.Success)
                weatherViewModel.loadWeather(currentLocation.latitude, currentLocation.longitude)
            }


        }

    }
}
