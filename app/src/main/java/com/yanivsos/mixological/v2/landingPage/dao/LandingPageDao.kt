package com.yanivsos.mixological.v2.landingPage.dao

import androidx.room.*
import com.yanivsos.mixological.domain.models.DrinkPreviewModel
import com.yanivsos.mixological.domain.models.LatestArrivalsModel
import com.yanivsos.mixological.domain.models.MostPopularModel
import com.yanivsos.mixological.domain.models.RecentlyViewedModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LandingPageDao {

    //Latest arrivals
    @Query("SELECT * FROM drink_previews INNER JOIN latest_arrivals ON drinkId = id")
    fun getLatestArrivals(): Flow<List<DrinkPreviewModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLatestArrivals(latestArrivals: List<LatestArrivalsModel>)

    @Query("DELETE FROM latest_arrivals")
    suspend fun removeLatestArrivals()

    @Transaction
    suspend fun replaceLatestArrivals(latestArrivals: List<LatestArrivalsModel>) {
        removeLatestArrivals()
        insertLatestArrivals(latestArrivals)
    }

    //Recently viewed
    @Query("SELECT * FROM drink_previews INNER JOIN recently_viewed ON drinkId = id")
    fun getRecentlyViewed(): Flow<List<DrinkPreviewModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecentlyViewed(recentlyViewed: List<RecentlyViewedModel>)

    @Query("DELETE FROM recently_viewed")
    suspend fun removeRecentlyViewed()

    @Transaction
    suspend fun replaceRecentlyViewed(recentlyViewed: List<RecentlyViewedModel>) {
        removeRecentlyViewed()
        insertRecentlyViewed(recentlyViewed)
    }

    //Most populars
    @Query("SELECT * FROM drink_previews INNER JOIN most_popular ON drinkId = id")
    fun getMostPopulars(): Flow<List<DrinkPreviewModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMostPopulars(mostPopulars: List<MostPopularModel>)

    @Query("DELETE FROM most_popular")
    suspend fun removeMostPopulars()

    @Transaction
    suspend fun replaceMostPopular(mostPopulars: List<MostPopularModel>) {
        removeMostPopulars()
        insertMostPopulars(mostPopulars)
    }
}
