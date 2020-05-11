package com.fatihucarci.routedrawingsample.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/*
* Copyright (C) Fatih UCARCI - All Rights Reserved
* Unauthorized copying of this file, via any medium is strictly prohibited
* Proprietary and confidential
* Written by Fatih UCARCI <fatih.ucarci@gmail.com>, March 2020 
*
* Created by Fatih UCARCI at 3/30/2020
*/

@Dao
interface RunActivityDAO {
    @Insert
    suspend fun addRun(runActivity: RunActivity)

    @Query("SELECT * FROM table_runactivity ORDER BY start_time DESC")
    suspend fun getRunActivities(): List<RunActivity>

    @Delete
    suspend fun deleteRun(runActivity: RunActivity)

    @Query("SELECT * FROM table_runactivity ORDER BY start_time DESC LIMIT 4")
    suspend fun getRecentRunActivities(): List<RunActivity>

    @Query("SELECT * from table_runactivity where id= :runId")
    suspend fun getRun(runId: Int) : RunActivity
}