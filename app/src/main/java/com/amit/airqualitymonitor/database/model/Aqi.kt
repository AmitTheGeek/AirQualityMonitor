package com.amit.airqualitymonitor.database.model

import android.graphics.Color
import androidx.core.content.ContextCompat.getColor
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "aqi_table")
data class Aqi (@PrimaryKey(autoGenerate = true)
                     var id: Int,
                @ColumnInfo(name = "city")var city: String,
                @ColumnInfo(name = "aqi") var aqi : Double,
                @ColumnInfo(name = "timestamp")
                      var timestamp: Long) {
    var prettyTime : String? = null

    var color : Int? = null

}