package com.yanivsos.mixological.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yanivsos.mixological.domain.models.AlcoholicFilterModel
import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.repo.room.MapStringToStringOptionalConverter
import com.yanivsos.mixological.repo.room.TABLE_NAME_ALCOHOLIC_FILTERS
import com.yanivsos.mixological.repo.room.TABLE_NAME_DRINKS

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

val MIGRATION_2_3 = object : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {
        val drinkModelTableName = DrinkModel::class.java.simpleName
        database.run {
            execSQL(
                "ALTER TABLE $drinkModelTableName"
                        + " RENAME TO $TABLE_NAME_DRINKS"
            )
            execSQL(
                "ALTER TABLE ${AlcoholicFilterModel::class.java.simpleName}"
                        + " RENAME TO $TABLE_NAME_ALCOHOLIC_FILTERS"
            )
        }
    }
}