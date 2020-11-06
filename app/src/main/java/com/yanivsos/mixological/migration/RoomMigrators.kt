package com.yanivsos.mixological.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yanivsos.mixological.domain.models.DrinkModel
import com.yanivsos.mixological.repo.room.*

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
        database.run {
            renameTable("IngredientModel", TABLE_NAME_INGREDIENTS)
            renameTable("GlassModel", TABLE_NAME_GLASSES)
            renameTable("AlcoholicFilterModel", TABLE_NAME_ALCOHOLIC_FILTERS)
            renameTable("WatchlistItemModel", TABLE_NAME_WATCHLIST)
            renameTable("IngredientDetailsModel", TABLE_NAME_INGREDIENTS_DETAILS)
            renameTable("CategoryModel", TABLE_NAME_CATEGORY)
            renameTable("DrinkPreviewModel", TABLE_NAME_DRINK_PREVIEWS)
            renameTable("LatestArrivalsModel", TABLE_NAME_LATEST_ARRIVALS)
            renameTable("MostPopularModel", TABLE_NAME_MOST_POPULAR)
            renameTable("RecentlyViewedModel", TABLE_NAME_RECENTLY_VIEWED)
            renameTable("DrinkModel", TABLE_NAME_DRINKS)
        }
    }

    private fun SupportSQLiteDatabase.renameTable(
        oldName: String, newName: String
    ) {
        execSQL(
            "ALTER TABLE $oldName"
                    + " RENAME TO $newName"
        )
    }
}