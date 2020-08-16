package com.yanivsos.mixological.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.repo.room.MapStringToStringOptionalConverter

val MIGRATION_1_2 = object : Migration(1, 2) {

    private val converter = MapStringToStringOptionalConverter()

    override fun migrate(database: SupportSQLiteDatabase) {
        val drinkModelTableName = DrinkModel::class.java.simpleName
        val emptyMapValue = converter.fromMap(emptyMap())
        database.run {
            execSQL(
                "ALTER TABLE $drinkModelTableName"
                        + " ADD COLUMN nameLocalsMap TEXT default '$emptyMapValue' NOT NULL"
            )

            execSQL(
                "ALTER TABLE $drinkModelTableName"
                        + " ADD COLUMN instructionsLocalsMap TEXT default '$emptyMapValue' NOT NULL"
            )
        }
    }
}