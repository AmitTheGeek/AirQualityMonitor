package com.amit.airqualitymonitor.data

import com.amit.airqualitymonitor.database.AppDatabase
import com.amit.airqualitymonitor.database.model.Aqi
import com.amit.airqualitymonitor.websocket.WebSocketCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LocalDataSource (private val db: AppDatabase, private val viewModelScope: CoroutineScope) : DataSource, WebSocketCallback {

    override fun getAllAqiData(callback: DataSource.GetAqiIndexCallback) {

        viewModelScope.launch(Dispatchers.IO) {

            callback.onOperationComplete(db.AqiDao().getAll())
        }
    }

    override fun getAqiDataGroupedByCity(callback: DataSource.GetAqiIndexCallback) {
        viewModelScope.launch(Dispatchers.IO) {

            callback.onOperationComplete(db.AqiDao().getLatestDataGroupByCity())
        }
    }

    override fun getSpecificCityAqiData(city: String, callback: DataSource.GetAqiIndexCallback) {
        viewModelScope.launch(Dispatchers.IO) {

            callback.onOperationComplete(db.AqiDao().getAqiByCity(city))
        }
    }

    override fun onMessageReceived(message: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            db.AqiDao().deleteOldData(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1))
            db.AqiDao().insertAll(parseMessage(message))
        }

    }

    private fun parseMessage(message: String?) : List<Aqi> {
        val gson = Gson()
        val aqiEntityType = object : TypeToken<List<Aqi>>() {}.type
        val aqiList: List<Aqi> = gson.fromJson(message, aqiEntityType)
        val timestamp  = System.currentTimeMillis()
        for(i in aqiList.indices)
            aqiList[i].timestamp = timestamp

        return aqiList
    }
}