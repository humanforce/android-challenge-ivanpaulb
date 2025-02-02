package com.humanforce.humanforceandroidengineeringchallenge.data.mapper

import com.humanforce.humanforceandroidengineeringchallenge.data.models.apiresponses.ForecastResponse
import com.humanforce.humanforceandroidengineeringchallenge.data.models.apiresponses.WeatherResponse
import com.humanforce.humanforceandroidengineeringchallenge.data.models.entities.ForecastEntity
import com.humanforce.humanforceandroidengineeringchallenge.data.models.entities.WeatherEntity
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.DailyForecast
import com.humanforce.humanforceandroidengineeringchallenge.domain.models.Weather
import com.humanforce.humanforceandroidengineeringchallenge.utils.WeatherUtils

fun WeatherResponse.toWeatherEntity() = WeatherEntity(
    temperature = main.temp,
    condition = weather[0].description,
    icon = weather[0].icon,
    windSpeed = wind.speed,
    feelsLike = main.feels_like,
    humidity = main.humidity,
    visibility = visibility / 1000.0,
    pressure = main.pressure
)

fun ForecastResponse.toDailyForecastListEntities() : List<ForecastEntity> {
    return list.groupBy { it.dt_txt.split(" ")[0] }
        .map { (date, forecasts) ->
            ForecastEntity(
                date = WeatherUtils.formatDateString(date),
                minTemp = forecasts.minOf { it.main.temp_min },
                maxTemp = forecasts.maxOf { it.main.temp_max },
                condition = forecasts[0].weather[0].description,
                icon = forecasts[0].weather[0].icon
            )
        }.take(5)
}

fun WeatherEntity.toDomain() =
    Weather(temperature, condition, icon, windSpeed, feelsLike, humidity, pressure, visibility )

fun ForecastEntity.toDomain() =
    DailyForecast( date, minTemp, maxTemp, condition, icon)