package com.yanivsos.mixological.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yanivsos.mixological.domain.models.*
import com.yanivsos.mixological.repo.room.*
import kotlin.reflect.KClass

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
            renameTable(IngredientModel::class, TABLE_NAME_INGREDIENTS)
            renameTable(GlassModel::class, TABLE_NAME_GLASSES)
            renameTable(AlcoholicFilterModel::class, TABLE_NAME_ALCOHOLIC_FILTERS)
            renameTable(WatchlistItemModel::class, TABLE_NAME_WATCHLIST)
            renameTable(IngredientDetailsModel::class, TABLE_NAME_INGREDIENTS_DETAILS)
            renameTable(CategoryModel::class, TABLE_NAME_CATEGORY)
            renameTable(DrinkPreviewModel::class, TABLE_NAME_DRINK_PREVIEWS)
            renameTable(LatestArrivalsModel::class, TABLE_NAME_LATEST_ARRIVALS)
            renameTable(MostPopularModel::class, TABLE_NAME_MOST_POPULAR)
            renameTable(RecentlyViewedModel::class, TABLE_NAME_RECENTLY_VIEWED)
            renameTable(DrinkModel::class, TABLE_NAME_DRINKS)
        }
    }

    private fun SupportSQLiteDatabase.renameTable(
        oldClass: KClass<*>, newName: String
    ) {
        val oldName = oldClass::class.java.simpleName
        execSQL(
            "ALTER TABLE $oldName"
                    + " RENAME TO $newName"
        )
    }
}