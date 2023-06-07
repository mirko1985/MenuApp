package com.mirko.menuapp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.mirko.menuapp.databinding.ActivityMainBinding
import com.mirko.menuapp.repositories.DirectoryRepository
import com.mirko.menuapp.repositories.retrofit.RetrofitHelper

class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 444

    private lateinit var binding: ActivityMainBinding
    private val networkRequest =
        NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.retrofitHelper = RetrofitHelper

        startNetworkMonitor()

        if (isLocationPermissionGranted()) {
            startLocationMonitoring()
        } else {
            requestLocationPermission()
        }
    }

    private fun startLocationMonitoring() {
        DirectoryRepository.initLocationServiceClient()
    }

    private fun startNetworkMonitor() {
        val connectivityManager =
            (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

        binding.networkLost =
            connectivityManager.activeNetwork == null || connectivityManager.getNetworkCapabilities(
                connectivityManager.activeNetwork
            ) == null

        connectivityManager.registerNetworkCallback(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                var connectedCount = 0

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    connectedCount++
                    binding.networkLost = false
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    if (connectedCount > 0) connectedCount--

                    if (connectedCount == 0)
                        binding.networkLost = true

                }
            }
        )

    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationMonitoring()
            } else {
                // Location permission denied
                // Handle the denial or disable location-related features
            }
        }
    }
}