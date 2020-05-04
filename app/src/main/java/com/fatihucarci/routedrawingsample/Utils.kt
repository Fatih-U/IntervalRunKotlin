package com.fatihucarci.routedrawingsample

import android.annotation.SuppressLint
import android.location.Location
import com.fatihucarci.routedrawingsample.room.RunActivity
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat


/*
* Copyright (C) Fatih UCARCI - All Rights Reserved
* Unauthorized copying of this file, via any medium is strictly prohibited
* Proprietary and confidential
* Written by Fatih UCARCI <fatih.ucarci@gmail.com>, March 2020 
*
* Created by Fatih UCARCI at 3/30/2020
*/

/**
 * String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
 */
fun calculateDuration(runItem: RunActivity) : String {

    var hours = "%02d".format(runItem.endTimeMilli!!.minus(runItem.startTimeMilli!!)/1000/60/60)
    var minutes = "%02d".format(runItem.endTimeMilli!!.minus(runItem.startTimeMilli!!)/1000/60)
    var seconds = "%02d".format(runItem.endTimeMilli!!.minus(runItem.startTimeMilli!!)/1000)

    return "$hours:$minutes:$seconds"
}


@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm:ss.SSS")
        .format(systemTime).toString()
}

@SuppressLint("SimpleDateFormat")
fun convertLongToSimpleString(systemTime: Long) : String {
    return SimpleDateFormat("EEEE MMM/dd/yy' 'HH:mm")
        .format(systemTime).toString()
}

fun calculateDistance(pathPoints: MutableList<LatLng>) : Float {
    var totalDistance = 0.0f
    var startLoc = Location("")
    var endLoc = Location("")
    var currentPoint = pathPoints.first()
    //startLoc.latitude = currentPoint.latitude
    //startLoc.longitude = currentPoint.longitude

    for (point in pathPoints) {
        if(point != pathPoints.first()) {
            startLoc.latitude = currentPoint.latitude
            startLoc.longitude = currentPoint.longitude
            endLoc.latitude = point.latitude
            endLoc.longitude = point.longitude
            totalDistance += startLoc.distanceTo(endLoc)
            currentPoint = point
        }
    }

    return totalDistance
}