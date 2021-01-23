package com.nishidhpatel.currentLocation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kishorramani.mymapdemo.viewmodel.LocationLiveData
import com.kishorramani.mymapdemo.model.LocationModel

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData = LocationLiveData(application)

    fun getLocationData(): LiveData<LocationModel> = locationData
}