package com.zemingo.drinksmenu.repo.reactive_store

import kotlinx.coroutines.flow.Flow

interface ReactiveStore<T> {

    fun getAll(): Flow<List<T>>

    fun storeAll(data: List<T>)
}