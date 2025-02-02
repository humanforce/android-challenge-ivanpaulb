package com.humanforce.humanforceandroidengineeringchallenge.data.api

import android.util.Log
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiCallHelper @Inject constructor() {
    object API_ERROR_MESSAGES {
        const val NETWORK_ERROR_MESSAGE= "Network error. Please check your internet connection."
        const val TIMEOUT_ERROR_MESSAGE= "Request timed out. Please try again."
        const val BAD_REQUEST_ERROR_MESSAGE= "Bad Request. Please check your input."
        const val UNAUTHORIZED_ERROR_MESSAGE= "Unauthorized. Please check your API key."
        const val FORBIDDEN_ERROR_MESSAGE= "Forbidden. You don’t have permission."
        const val NOT_FOUND_ERROR_MESSAGE= "Not Found. The requested resource doesn’t exist."
        const val SERVER_ERROR_MESSAGE= "Server error. Please try again later."
        const val GENERIC_ERROR_MESSAGE= "Something went wrong. Please try again."
    }
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return try {
            val response = apiCall()
            Resource.Success(response)
        } catch (e: IOException) { // Network errors (No internet, etc.)
            Log.e("ApiCallHelper", "Network Error: ${e.message}")
            Resource.Error(API_ERROR_MESSAGES.NETWORK_ERROR_MESSAGE)
        } catch (e: SocketTimeoutException) { // Request timeout
            Log.e("ApiCallHelper", "Timeout Error: ${e.message}")
            Resource.Error(API_ERROR_MESSAGES.TIMEOUT_ERROR_MESSAGE)
        } catch (e: HttpException) { // HTTP errors (4xx, 5xx)
            Log.e("ApiCallHelper", "HTTP Error: ${e.message}")
            val errorMessage = when (e.code()) {
                400 -> API_ERROR_MESSAGES.BAD_REQUEST_ERROR_MESSAGE
                401 -> API_ERROR_MESSAGES.UNAUTHORIZED_ERROR_MESSAGE
                403 -> API_ERROR_MESSAGES.FORBIDDEN_ERROR_MESSAGE
                404 -> API_ERROR_MESSAGES.NOT_FOUND_ERROR_MESSAGE
                500 -> API_ERROR_MESSAGES.SERVER_ERROR_MESSAGE
                else -> "Unexpected error: ${e.message}"
            }
            Resource.Error(errorMessage)
        } catch (e: Exception) { // Any other unexpected errors
            Log.e("ApiCallHelper", "Unexpected Error: ${e.message}")
            Resource.Error(API_ERROR_MESSAGES.GENERIC_ERROR_MESSAGE)
        }
    }
}
