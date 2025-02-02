package com.humanforce.humanforceandroidengineeringchallenge.presentation.ui

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.humanforce.humanforceandroidengineeringchallenge.R
import com.humanforce.humanforceandroidengineeringchallenge.data.api.Resource
import com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel.LocationState
import com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel.LocationViewModel
import com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel.WeatherViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDashboard(
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val weatherState by weatherViewModel.currWeatherState.collectAsState()
    val forecastsState by weatherViewModel.forecastsState.collectAsState()
    val locationState by locationViewModel.locationState.collectAsState()
    val searchResultsState by locationViewModel.searchResults.collectAsState()

    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var searchCityText by remember { mutableStateOf("") }

    PullToRefreshBox(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            locationViewModel.retrieveCurrentLocationState()
        },
        state = state,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = state
            )
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            if (isRefreshing) {
                CircularProgressIndicator(context)
            }
            TextField(
                value = searchCityText,
                onValueChange = {
                    searchCityText = it
                    if(!it.isEmpty()) {
                        locationViewModel.searchLocation(it)
                    }
                },
                label = { Text(stringResource(R.string.search_city_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            if (searchCityText.isEmpty()) {
                when (weatherState) {
                    is Resource.Loading -> CircularProgressIndicator(context)
                    is Resource.Success -> {
                        isRefreshing = false

                        val weatherData = weatherState.data
                        if (weatherData != null) {
                            WeatherWidget(
                                weatherDetails = weatherData,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    is Resource.Error -> {
                        isRefreshing = false
                        Toast.makeText(
                            LocalContext.current, (weatherState as Resource.Error).message,
                            Toast.LENGTH_SHORT
                        ).show()
                        val weatherData = weatherState.data
                        if (weatherData != null) {
                            WeatherWidget(
                                weatherDetails = weatherData,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                when (forecastsState) {
                    is Resource.Loading -> CircularProgressIndicator(context)
                    is Resource.Success -> {
                        isRefreshing = false

                        val forecastData = forecastsState.data
                        if (forecastData != null) {
                            ForecastWidget(
                                forecastDetails = forecastData,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    is Resource.Error -> {
                        isRefreshing = false
                        Toast.makeText(
                            LocalContext.current, (forecastsState as Resource.Error).message,
                            Toast.LENGTH_SHORT
                        ).show()
                        val forecastData = forecastsState.data
                        if (forecastData != null) {
                            ForecastWidget(
                                forecastDetails = forecastData,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

            } else {
                when (searchResultsState) {
                    is Resource.Loading -> CircularProgressIndicator(context)
                    is Resource.Success -> {
                        val searchResults = searchResultsState.data
                        if(searchResults != null) {
                            Column {
                                for (location in searchResults) {
                                    Text(
                                        modifier = Modifier.padding(5.dp)
                                            .clickable {
                                                searchCityText = ""
                                                locationViewModel
                                                    .updateLocationState(
                                                        LocationState.Success(
                                                            location.lat, location.long))
                                            },
                                        text = location.city,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(
                            LocalContext.current, (searchResultsState as Resource.Error).message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            }




            LaunchedEffect(locationState) {
                isRefreshing = false
                getCurrentWeather(locationState, weatherViewModel)
                getForecasts(locationState, weatherViewModel)
            }

        }
    }

}


fun checkLocationStateThenDoAction(
    locationState: LocationState,
    doActionWithLocation: (location: LocationState.Success) -> Unit
) {
    when (locationState) {
        is LocationState.Success -> {
            locationState.let {
                doActionWithLocation(locationState)
            }
        }

        else -> {
            //do nothing for now
        }
    }
}

fun getCurrentWeather(locationState: LocationState, weatherViewModel: WeatherViewModel) {
    checkLocationStateThenDoAction(locationState, doActionWithLocation = { currentLocation ->
        weatherViewModel.loadWeather(currentLocation.latitude, currentLocation.longitude)
    })
}


fun getForecasts(locationState: LocationState, weatherViewModel: WeatherViewModel) {
    checkLocationStateThenDoAction(locationState, doActionWithLocation = { currentLocation ->
        weatherViewModel.loadForecasts(currentLocation.latitude, currentLocation.longitude)
    })
}


