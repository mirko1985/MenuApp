package com.mirko.menuapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mirko.menuapp.MenuApp
import com.mirko.menuapp.repositories.retrofit.NetworkResponse
import com.mirko.menuapp.R
import com.mirko.menuapp.data.directory.Venue
import com.mirko.menuapp.repositories.DirectoryRepository
import kotlinx.coroutines.launch

class DirectoryViewModel : ViewModel() {

    data class VenueInfo(
        val name: String?,
        val distance: String?,
        val address: String?,
        val info: String?
    )

    data class VenueDetails(
        val name: String?,
        val description: String?,
        val is_open: String?,
        val welcome_message: String?,
        val image: String?
    )

    val venueDetailsList = MutableLiveData<List<Venue>?>()
    val venuesInfoList = venueDetailsList.map { list ->
        list?.map {
            VenueInfo(
                it.venue?.name,
                "${it.distance} m",
                it.venue?.address,
                if (it.venue?.is_open == true) MenuApp.instance.getString(R.string.opened)
                else MenuApp.instance.getString(R.string.closed)
            )
        }?.distinct()
    }

    fun venuesList() {
        viewModelScope.launch {
            DirectoryRepository.venuesList().collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        venueDetailsList.value = response.body.data?.venues
                    }
                    else -> {}
                }
            }
        }
    }
}