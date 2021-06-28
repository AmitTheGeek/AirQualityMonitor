package com.amit.airqualitymonitor.ui

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.amit.airqualitymonitor.R
import com.robinhood.spark.SparkView

class GraphActivity :AppCompatActivity() {

    companion object {
        const val TAG = "GraphActivity"
        const val CITY = "city"
    }
    private lateinit var viewModel: GraphViewModel
    private lateinit var city : String
    private lateinit var sparkView: SparkView

    private val delay = 30000L
    private lateinit var handler : Handler

    private val runnableCode = object: Runnable {
        override fun run() {
            viewModel.loadAqiByCity(city)
            handler.postDelayed(this, delay)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        city = intent.getStringExtra(CITY) as String
        viewModel = ViewModelProvider(this).get(GraphViewModel::class.java)
        sparkView = findViewById(R.id.sparkview)

        viewModel.getCityAqiData().observe(this, {
            Log.d(TAG, "onCreate: $it")

            if (it != null) {
                sparkView.adapter = GraphAdapter(it)
            }
        })

        viewModel.initRepository()
        handler = Handler()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        viewModel.openWebSocketConnection()
        handler.postDelayed(runnableCode, 0)
    }

    override fun onPause() {
        super.onPause()
        viewModel.closeWebSocketConnection()
        handler.removeCallbacks(runnableCode)
    }

}