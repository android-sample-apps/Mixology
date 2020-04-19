package com.zemingo.drinksmenu.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zemingo.drinksmenu.models.CategoryModel

@Database(entities = [CategoryModel::class], version = 1)
abstract class DrinksDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}