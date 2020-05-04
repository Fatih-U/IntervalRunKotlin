package com.fatihucarci.routedrawingsample.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

/*
* Copyright (C) Fatih UCARCI - All Rights Reserved
* Unauthorized copying of this file, via any medium is strictly prohibited
* Proprietary and confidential
* Written by Fatih UCARCI <fatih.ucarci@gmail.com>, March 2020 
*
* Created by Fatih UCARCI at 3/30/2020
*/


@Entity(tableName = "table_runactivity")
data class RunActivity (

    @ColumnInfo(name = "start_time")
    var startTimeMilli : Long?,

    @ColumnInfo(name = "end_time")
    var endTimeMilli : Long?,

    @ColumnInfo(name = "path_points")
    var pathPoints : MutableList<LatLng>

) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}