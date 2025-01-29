package com.humanforce.humanforceandroidengineeringchallenge.utils

import java.text.SimpleDateFormat
import java.util.Locale

object WeatherUtils {

    fun formatWeatherIconUrl(iconName: String): String {
        return "https://openweathermap.org/img/w/${iconName}.png"
    }

    fun capitalizeFirstLetter(text: String): String {
        return text.replaceFirstChar { it.uppercase() }
    }

    fun formatTempToCelsiusString(temp: Double): String {
        return "${temp}°C"
    }

    fun formatDateString(dateString: String): String {
        try {
            val inputFormat = "yyyy-MM-dd"
            val outputFormat = "MMM dd, yyyy"

            val inputDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
            val outputDateFormat = SimpleDateFormat(outputFormat, Locale.getDefault())
            val date = inputDateFormat.parse(dateString)
            return outputDateFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}