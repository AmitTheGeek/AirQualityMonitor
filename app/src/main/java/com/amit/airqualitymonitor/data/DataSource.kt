package com.amit.airqualitymonitor.data

import androidx.lifecycle.LiveData
import com.amit.airqualitymonitor.database.model.Aqi

interface DataSource {

    interface GetAqiIndexCallback {

        fun onOperationComplete(aqiIndexes: List<Aqi>)

        fun onOperationFailed(t: Throwable? = Throwable())
    }

    fun getAllAqiData(callback: GetAqiIndexCallback)

    fun getAqiDataGroupedByCity(callback: GetAqiIndexCallback)

    fun getSpecificCityAqiData(city : String, callback: GetAqiIndexCallback)
}