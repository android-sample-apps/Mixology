package com.zemingo.drinksmenu.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zemingo.drinksmenu.domain.models.DrinkModel
import com.zemingo.drinksmenu.repo.repositories.DrinkRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DrinkViewModel(
    private val drinkRepository: DrinkRepository
) : ViewModel() {

    private val _drink = MutableLiveData<DrinkModel>()
    val drink: LiveData<DrinkModel> = _drink

    fun getById(id: String) {
        viewModelScope.launch {
            drinkRepository
                .get(id)
                .collect { _drink.postValue(it) }
        }
    }
}