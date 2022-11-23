package com.example.clock.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.clock.data.model.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Update
    suspend fun update(alarm: Alarm)

    @Query("DELETE FROM alarms_list_table")
    suspend fun clear()

    @Query("SELECT * FROM alarms_list_table ORDER BY id DESC")
    fun getAlarmsList(): Flow<List<Alarm>>

    @Query("SELECT * FROM alarms_list_table WHERE id=:id")
    suspend fun getAlarmById(id: Int): Alarm?

    @Query("SELECT id FROM alarms_list_table ORDER BY id DESC LIMIT 1")
    suspend fun getLastAutoId(): Int?

    @Query("SELECT * FROM alarms_list_table WHERE hour=:hour AND minute=:minute AND isRecurring=:recurring")
    fun getAlarmByTime(hour: String, minute: String, recurring: Boolean): Flow<Alarm?>
}