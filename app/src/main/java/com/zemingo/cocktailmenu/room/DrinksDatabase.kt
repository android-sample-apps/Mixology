package com.zemingo.cocktailmenu.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zemingo.cocktailmenu.models.CategoryEntity

@Database(entities = [CategoryEntity::class], version = 1)
abstract class DrinksDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}