package com.fatihucarci.routedrawingsample

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import androidx.core.content.ContextCompat
import com.fatihucarci.routedrawingsample.room.RunActivity
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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

    var diff = runItem.endTimeMilli!!.minus(runItem.startTimeMilli!!)


    var hours = "%02d".format(diff/(3600*1000))
    var minutes = "%02d".format(diff/(60*1000) % 60)
    var seconds = "%02d".format(diff/1000 % 60)

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


/**
 * Calculating total distance of running path in meters
 * Baslangic noktasindan itibaren kosulan yolun uzunlugu metre olarak hesaplaniyor
 */
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

/**
 *
 */
fun  bitmapDescriptorFromVector(context: Context, vectorResId:Int): BitmapDescriptor {
    var vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
    vectorDrawable!!.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
    var bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    var canvas =  Canvas(bitmap);
    vectorDrawable.draw(canvas);
    return BitmapDescriptorFactory.fromBitmap(bitmap);
}