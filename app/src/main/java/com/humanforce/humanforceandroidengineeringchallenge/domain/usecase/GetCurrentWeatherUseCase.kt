package com.humanforce.humanforceandroidengineeringchallenge.domain.usecase

import com.humanforce.humanforceandroidengineeringchallenge.domain.models.Weather
import com.humanforce.humanforceandroidengineeringchallenge.domain.repo.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): Weather {
        return repository.getCurrentWeather(lat, lon)
    }
}
