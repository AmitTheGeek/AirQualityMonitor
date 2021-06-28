package com.amit.airqualitymonitor.ui

import com.robinhood.spark.SparkAdapter

class GraphAdapter(private val yData: List<Float>) : SparkAdapter() {
    override fun getCount(): Int {
        return yData.size
    }

    override fun getItem(index: Int): Any {
        return yData[index]
    }

    override fun getY(index: Int): Float {
        return yData[index]
    }

}