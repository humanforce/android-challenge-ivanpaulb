package com.humanforce.humanforceandroidengineeringchallenge.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.DailyForecast
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.Weather
import com.humanforce.humanforceandroidengineeringchallenge.domain.usecase.GetCurrentWeatherUseCase
import com.humanforce.humanforceandroidengineeringchallenge.domain.usecase.GetFiveDayForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getFiveDayForecastUseCase: GetFiveDayForecastUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()


    private var _searchQuery = mutableStateOf("")
    val searchQuery: State<String> get() = _searchQuery

    fun searchQuery(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentWeather = getCurrentWeatherUseCase(lat, lon)
                val forecast = getFiveDayForecastUseCase(lat, lon)
                _uiState.value = WeatherUiState.Success(currentWeather, forecast)
            } catch (e: Exception) {
                e.message?.let { Log.e("HumanForceDebug", it) }
                _uiState.value = WeatherUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}

sealed class WeatherUiState {
    data object Loading : WeatherUiState()
    data class Success(val weather: Weather, val forecast: List<DailyForecast>) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
