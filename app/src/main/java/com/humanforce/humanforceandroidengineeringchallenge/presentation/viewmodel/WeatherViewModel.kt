package com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.Weather
import com.humanforce.humanforceandroidengineeringchallenge.domain.usecase.GetCurrentWeatherUseCase
import com.humanforce.humanforceandroidengineeringchallenge.domain.usecase.GetFiveDayForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import com.humanforce.humanforceandroidengineeringchallenge.data.api.Resource
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.DailyForecast
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getFiveDayForecastUseCase: GetFiveDayForecastUseCase
) : ViewModel() {

    private val _currWeatherState = MutableStateFlow<Resource<Weather>>(Resource.Loading)
    val currWeatherState: StateFlow<Resource<Weather>> = _currWeatherState.asStateFlow()

    private val _forecastsState = MutableStateFlow<Resource<List<DailyForecast>>>(Resource.Loading)
    val forecastsState: StateFlow<Resource<List<DailyForecast>>> = _forecastsState.asStateFlow()


    fun loadWeather(lat: Double, lon: Double) {
        _currWeatherState.value = Resource.Loading
        viewModelScope.launch(Dispatchers.IO) {
                _currWeatherState.value = getCurrentWeatherUseCase(lat, lon)
        }
    }

    fun loadForecasts(lat: Double, lon: Double) {
        _forecastsState.value = Resource.Loading
        viewModelScope.launch(Dispatchers.IO) {
            _forecastsState.value = getFiveDayForecastUseCase(lat, lon)
        }
    }
}
