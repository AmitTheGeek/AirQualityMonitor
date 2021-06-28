package com.amit.airqualitymonitor.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.amit.airqualitymonitor.R
import com.amit.airqualitymonitor.database.model.Aqi
import com.amit.airqualitymonitor.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AqiViewModel
    private lateinit var listAdapter: AqiAdapter
    private val delay = 30000L
    private lateinit var handler : Handler
    companion object {
        const val TAG = "MainActivity"
    }

    private val runnableCode = object: Runnable {
        override fun run() {
            viewModel.loadAqiIndexes()
            handler.postDelayed(this, delay)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(AqiViewModel::class.java)
        viewModel.initRepository()
        binding.viewmodel = viewModel

        viewModel.apply {
            openCityAqiDetails.observe(this@MainActivity, { aqi ->
                openGraphActivity(aqi)
            } )
        }

        setAqiListAdapter()
        binding.executePendingBindings()

        handler = Handler()
    }

    private fun openGraphActivity(aqi: Aqi){
        val intent = Intent(this, GraphActivity::class.java).apply {
            putExtra(GraphActivity.CITY, aqi.city)
        }
        startActivity(intent)

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

    private fun setAqiListAdapter() {
        listAdapter = AqiAdapter(ArrayList(0), viewModel)
        with(binding.aqiRecyclerView) {
            val layoutManager = LinearLayoutManager(context)
            setLayoutManager(layoutManager)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
                setDrawable(ContextCompat.getDrawable(context, R.drawable.list_item_divider)!!)
            }
            addItemDecoration(dividerItemDecoration)
            adapter = listAdapter
        }

    }
}