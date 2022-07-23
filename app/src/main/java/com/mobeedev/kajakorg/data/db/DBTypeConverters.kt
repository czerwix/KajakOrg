package com.mobeedev.kajakorg.data.db

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import java.time.ZonedDateTime


class DBTypeConverters {

    @TypeConverter
    fun zonedDateTimeToString(date: ZonedDateTime): String = date.toString()

    @TypeConverter
    fun stringToZonedDateTime(date: String): ZonedDateTime = try {
        ZonedDateTime.parse(date)
    } catch (e: Exception) {
        ZonedDateTime.now()
    }

    @TypeConverter
    fun LatLngDateTimeToString(latLng: LatLng): String = "${latLng.latitude}*${latLng.longitude}"

    @TypeConverter
    fun stringToLatLng(latLng: String): LatLng = try {
        val position = latLng.split("*")

        LatLng(position[0].toDouble(),position[1].toDouble())
    } catch (e: Exception) {
        LatLng(0.toDouble(),0.toDouble())
    }
}