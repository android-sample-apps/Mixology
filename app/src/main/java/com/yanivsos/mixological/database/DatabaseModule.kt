
package com.yanivsos.mixological.database

import androidx.room.Room
import com.yanivsos.mixological.migration.MIGRATION_1_2
import com.yanivsos.mixological.migration.MIGRATION_2_3
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            DrinksDatabase::class.java, "drinks-db"
        )
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .build()
    }

    factory { get<DrinksDatabase>().getCategoryDao() }

    factory { get<DrinksDatabase>().getAlcoholicFilters() }

    factory { get<DrinksDatabase>().getGlassDao() }

}
