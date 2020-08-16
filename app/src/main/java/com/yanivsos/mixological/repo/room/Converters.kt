package com.yanivsos.mixological.repo.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapStringToStringOptionalConverter {

    @TypeConverter
    fun fromMap(map: Map<String, String?>): String {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun toMap(map: String): Map<String, String?> {
        val mapType = object : TypeToken<Map<String, String?>>() {}.type
        return Gson().fromJson(map, mapType)
    }
}