package com.amit.airqualitymonitor.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amit.airqualitymonitor.database.model.Aqi
import java.sql.Timestamp

@Dao
interface AqiDao {
    @Query("SELECT * FROM aqi_table")
    suspend fun getAll(): List<Aqi>

    @Query("SELECT * FROM aqi_table WHERE city LIKE :city")
    suspend fun getAqiByCity(city: String): List<Aqi>

    @Insert
    suspend fun insertAll(aqiEntity: List<Aqi>) : List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(aqiEntity: Aqi)

    @Delete
    suspend fun delete(aqiEntity: Aqi)

    @Query("DELETE FROM aqi_table WHERE timestamp <= :timestamp")
    suspend fun deleteOldData(timestamp: Long)

    @Query("SELECT * FROM aqi_table WHERE id IN (Select MAX(id) FROM aqi_table GROUP BY city) ORDER BY timestamp")
    suspend fun getLatestDataGroupByCity(): List<Aqi>
}

