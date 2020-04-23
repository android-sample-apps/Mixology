package com.zemingo.drinksmenu.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zemingo.drinksmenu.models.PreviousSearchModel
import com.zemingo.drinksmenu.models.SearchResultModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDrinkPreviewDao {

    @Query("SELECT * FROM PreviousSearchModel")
    fun getAll(): Flow<List<PreviousSearchModel>>

    @Query("SELECT PreviousSearchModel.* ,DrinkPreviewModel.* FROM PreviousSearchModel INNER JOIN DrinkPreviewModel ON id = drinkId")
    fun getAllWithDrink(): Flow<List<SearchResultModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(searches: List<PreviousSearchModel>)

}