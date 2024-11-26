package com.example.diaryprogram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class LocationViewModel : ViewModel() {
    private val _selectedLocation = MutableLiveData<LatLng?>()
    val selectedLocation: LiveData<LatLng?> = _selectedLocation

    fun setSelectedLocation(location: LatLng) {
        _selectedLocation.value = location
    }

    fun clearLocation() {
        _selectedLocation.value = null
    }
}