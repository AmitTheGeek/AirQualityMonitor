package com.amit.airqualitymonitor.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amit.airqualitymonitor.data.DataSource
import com.amit.airqualitymonitor.data.LocalDataSource
import com.amit.airqualitymonitor.data.Repository
import com.amit.airqualitymonitor.database.AppDatabase
import com.amit.airqualitymonitor.database.model.Aqi
import com.amit.airqualitymonitor.websocket.WebSocketHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class GraphViewModel(application: Application) : AndroidViewModel(application)  {

    companion object {
        const val TAG = "GraphViewModel"
    }
    private lateinit var repository: Repository
    private lateinit var webSocketHandler: WebSocketHandler
    private lateinit var db : AppDatabase
    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    val mutableAqiList = MutableLiveData<List<Float>>()

    fun initRepository(){
        db = AppDatabase(getApplication())
        val localDataSource = LocalDataSource(db, viewModelScope)
        webSocketHandler = WebSocketHandler(localDataSource)
        repository = Repository.getInstance(localDataSource)
    }

    fun openWebSocketConnection(){
        webSocketHandler.startConnection()
    }

    fun closeWebSocketConnection() {
        webSocketHandler.stopConnection()
    }

    fun loadAqiByCity(city :String) {
        repository.getSpecificCityAqiData(city, object : DataSource.GetAqiIndexCallback {
            override fun onOperationComplete(aqiIndexes: List<Aqi>) {
                Log.d(TAG, "testing onOperationComplete: ${aqiIndexes.size}")
                val aqiList = arrayListOf<Float>()
                for (i in aqiIndexes.indices)
                    aqiList.add(aqiIndexes[i].aqi.toFloat())

                mutableAqiList.postValue(aqiList)
            }

            override fun onOperationFailed(t: Throwable?) {
                Log.d(TAG, "onOperationFailed: $t")
            }
        })
    }

    fun getCityAqiData() : LiveData<List<Float>> {
        return mutableAqiList
    }

}