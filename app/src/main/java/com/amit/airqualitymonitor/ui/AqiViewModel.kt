package com.amit.airqualitymonitor.ui

import android.app.Application
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableList
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.amit.airqualitymonitor.data.DataSource
import com.amit.airqualitymonitor.data.LocalDataSource
import com.amit.airqualitymonitor.data.Repository
import com.amit.airqualitymonitor.database.AppDatabase
import com.amit.airqualitymonitor.database.model.Aqi
import com.amit.airqualitymonitor.util.SingleLiveEvent
import com.amit.airqualitymonitor.websocket.WebSocketHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class AqiViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "AqiViewModel"
        @JvmStatic
        @BindingAdapter("bind:items")
        fun entries(recyclerView: RecyclerView, aqiList: List<Aqi>) =
            aqiList?.let { (recyclerView.adapter as AqiAdapter).addToList(it) }

    }

    val dataLoading = ObservableBoolean(false)
    val items: ObservableList<Aqi> = ObservableArrayList()
    private lateinit var repository: Repository
    private lateinit var webSocketHandler: WebSocketHandler
    internal val openCityAqiDetails = SingleLiveEvent<Aqi>()
    private lateinit var db : AppDatabase
    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

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

    fun loadAqiIndexes() {
        dataLoading.set(true)
        items.clear()
        repository.getAqiDataGroupedByCity(object : DataSource.GetAqiIndexCallback {
            override fun onOperationComplete(aqiList: List<Aqi>) {
                with(items) {
                    Log.d(TAG, "onOperationComplete: ${aqiList.size}" )
                    addAll(aqiList)
                }
                dataLoading.set(false)
            }

            override fun onOperationFailed(t: Throwable?) {
                Log.d(TAG, "onOperationFailed: $t" )
                dataLoading.set(false)
            }
        })
    }

}