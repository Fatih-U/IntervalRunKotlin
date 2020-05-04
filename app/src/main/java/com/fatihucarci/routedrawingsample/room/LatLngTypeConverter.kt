package com.fatihucarci.routedrawingsample.room

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

/*
* Copyright (C) Fatih UCARCI - All Rights Reserved
* Unauthorized copying of this file, via any medium is strictly prohibited
* Proprietary and confidential
* Written by Fatih UCARCI <fatih.ucarci@gmail.com>, March 2020 
*
* Created by Fatih UCARCI at 3/30/2020
*/

/**
 * To save list data in room database, Type Converter is required...
 */
class LatLngTypeConverter {

    @TypeConverter
    fun listToJson(value: MutableList<LatLng>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<LatLng>::class.java).toList()
}