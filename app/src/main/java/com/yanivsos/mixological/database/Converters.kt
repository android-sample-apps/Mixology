package com.yanivsos.mixological.database

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MapStringToStringOptionalConverter {

    @TypeConverter
    fun fromMap(map: Map<String, String?>): String {
        // TODO: 21/04/2021 test this
        return Json.encodeToString(map)
    }

    @TypeConverter
    fun toMap(map: String): Map<String, String?> {
        // TODO: 21/04/2021 test this
        return Json.decodeFromString(map)
    }
}

