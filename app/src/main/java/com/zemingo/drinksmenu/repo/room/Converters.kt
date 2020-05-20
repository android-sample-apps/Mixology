package com.zemingo.drinksmenu.repo.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromIngredients(ingredients: Map<String, String?>): String {
        return Gson().toJson(ingredients)
    }

    @TypeConverter
    fun toIngredients(ingredients: String): Map<String, String?> {
        val mapType = object : TypeToken<Map<String, String?>>() {}.type
        return Gson().fromJson(ingredients, mapType)
    }
}