package com.mobeedev.kajakorg.data.db

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobeedev.kajakorg.ui.model.ChecklistValueItem
import java.time.ZonedDateTime
import java.util.UUID


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

        LatLng(position[0].toDouble(), position[1].toDouble())
    } catch (e: Exception) {
        LatLng(0.toDouble(), 0.toDouble())
    }

    @TypeConverter
    fun fromUUID(uuid: UUID): String? = uuid.toString()

    @TypeConverter
    fun uuidFromString(string: String?): UUID? = UUID.fromString(string)

    @TypeConverter
    fun fromCheckListList(checklistList: List<ChecklistValueItem>): String? {
        if (checklistList.isNullOrEmpty()) {
            return null
        }
        return Gson().toJson(checklistList, object : TypeToken<List<ChecklistValueItem?>?>() {}.type)
    }

    @TypeConverter
    fun toCheckListList(checklistString: String?): List<ChecklistValueItem> {
        if (checklistString.isNullOrEmpty()) {
            return emptyList()
        }
        return Gson().fromJson(checklistString, object : TypeToken<List<ChecklistValueItem>>() {}.type)
    }
}