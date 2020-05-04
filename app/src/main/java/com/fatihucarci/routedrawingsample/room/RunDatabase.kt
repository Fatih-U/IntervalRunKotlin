package com.fatihucarci.routedrawingsample.room

import android.content.Context
import androidx.room.*
import com.fatihucarci.routedrawingsample.MapsActivity
import kotlinx.coroutines.InternalCoroutinesApi

/*
* Copyright (C) Fatih UCARCI - All Rights Reserved
* Unauthorized copying of this file, via any medium is strictly prohibited
* Proprietary and confidential
* Written by Fatih UCARCI <fatih.ucarci@gmail.com>, March 2020 
*
* Created by Fatih UCARCI at 3/30/2020
*/

@Database(
    entities = [RunActivity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LatLngTypeConverter::class)
abstract class RunDatabase : RoomDatabase(){

    abstract fun getRunActivityDAO() : RunActivityDAO

    companion object {
        @Volatile private var instance: RunDatabase? = null
        private val LOCK = Any()

        @InternalCoroutinesApi
        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext,
            RunDatabase::class.java, "runactivities.db")
            .build()
    }

}