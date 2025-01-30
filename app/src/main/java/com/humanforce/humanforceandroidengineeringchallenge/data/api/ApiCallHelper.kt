package com.humanforce.humanforceandroidengineeringchallenge.data.api

import android.util.Log
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiCallHelper @Inject constructor() {
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return try {
            val response = apiCall()
            Resource.Success(response)
        } catch (e: IOException) { // Network errors (No internet, etc.)
            Log.e("ApiCallHelper", "Network Error: ${e.message}")
            Resource.Error("Network error. Please check your internet connection.")
        } catch (e: SocketTimeoutException) { // Request timeout
            Log.e("ApiCallHelper", "Timeout Error: ${e.message}")
            Resource.Error("Request timed out. Please try again.")
        } catch (e: HttpException) { // HTTP errors (4xx, 5xx)
            Log.e("ApiCallHelper", "HTTP Error: ${e.message}")
            val errorMessage = when (e.code()) {
                400 -> "Bad Request. Please check your input."
                401 -> "Unauthorized. Please check your API key."
                403 -> "Forbidden. You don’t have permission."
                404 -> "Not Found. The requested resource doesn’t exist."
                500 -> "Server error. Please try again later."
                else -> "Unexpected error: ${e.message}"
            }
            Resource.Error(errorMessage)
        } catch (e: Exception) { // Any other unexpected errors
            Log.e("ApiCallHelper", "Unexpected Error: ${e.message}")
            Resource.Error("Something went wrong. Please try again.")
        }
    }
}
