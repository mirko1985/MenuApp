package com.mirko.menuapp.repositories

import android.annotation.SuppressLint
import android.location.Location
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mirko.menuapp.MenuApp
import com.mirko.menuapp.repositories.retrofit.NetworkResponse
import com.mirko.menuapp.repositories.retrofit.RetrofitHelper
import com.mirko.menuapp.data.directory.DirectorySearchRequest
import com.mirko.menuapp.data.directory.DirectorySearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

object DirectoryRepository {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var currentLocation: Location? = null

    suspend fun venuesList(): Flow<NetworkResponse<DirectorySearchResponse, Any>> {
        return flow {
            val directorySearchRequest = if (currentLocation != null) DirectorySearchRequest(
                currentLocation?.latitude.toString(),
                currentLocation?.longitude.toString()
            )
            else DirectorySearchRequest("44.001783", "21.26907")

            emit(RetrofitHelper.directoryService.venuesList(directorySearchRequest))

        }.flowOn(Dispatchers.IO)
            .catch { error ->
                withContext(Dispatchers.Main) {
                    Toast.makeText(MenuApp.instance, error.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    @SuppressLint("MissingPermission")
    fun initLocationServiceClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MenuApp.instance)

        fusedLocationClient.lastLocation.addOnSuccessListener {
            currentLocation = it
        }
    }
}