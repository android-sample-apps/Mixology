package com.yanivsos.mixological.v2.drink.useCases

import com.yanivsos.mixological.v2.drink.dao.FavoriteDrinksDao

class GetFavoriteDrinksUseCase(
    dao: FavoriteDrinksDao
) {
    val favoriteDrinks = dao.getFavorites()
}
