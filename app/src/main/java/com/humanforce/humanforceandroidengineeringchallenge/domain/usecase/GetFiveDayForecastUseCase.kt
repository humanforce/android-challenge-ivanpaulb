package com.humanforce.humanforceandroidengineeringchallenge.domain.usecase

import com.humanforce.humanforceandroidengineeringchallenge.domain.models.DailyForecast
import com.humanforce.humanforceandroidengineeringchallenge.domain.repo.WeatherRepository
import javax.inject.Inject

class GetFiveDayForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): List<DailyForecast> {
        return repository.getFiveDayForecast(lat, lon)
    }
}
