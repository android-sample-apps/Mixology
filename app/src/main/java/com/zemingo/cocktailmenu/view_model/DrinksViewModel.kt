package com.zemingo.cocktailmenu.view_model

import android.util.Log
import androidx.lifecycle.*
import com.zemingo.cocktailmenu.models.*
import com.zemingo.cocktailmenu.repo.CocktailRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.function.Function

private const val TAG = "DrinksViewModel"

class DrinksViewModel(
    private val drinksRepository: CocktailRepository,
    private val drinkMapper: Function<DrinkModel, DrinkItemUiModel>,
    private val drinkPreviewMapper: Function<DrinkPreviewListModel, DrinksPreviewListItemUiModel>
) : ViewModel() {

    private val _randomCocktailLiveData = MutableLiveData<DrinkItemUiModel>()
    val randomCocktailLiveData: LiveData<DrinkItemUiModel> = _randomCocktailLiveData

    private val _cocktailMenuLiveData = MutableLiveData<DrinksPreviewListItemUiModel>()
    val cocktailMenuLiveData: LiveData<DrinksPreviewListItemUiModel> = _cocktailMenuLiveData

    fun randomCocktail() {
        viewModelScope.launch {
            drinksRepository
                .random()
                .map { drinkMapper.apply(it) }
                .catch { Log.e(TAG, "Unable to get random cocktail", it) }
                .collect { _randomCocktailLiveData.postValue(it) }
        }
    }

    fun getCocktailMenu() {
        viewModelScope.launch {
            drinksRepository
                .cocktails()
                .map { drinkPreviewMapper.apply(it) }
                .catch { }
                .collect { _cocktailMenuLiveData.postValue(it) }
        }
    }
}