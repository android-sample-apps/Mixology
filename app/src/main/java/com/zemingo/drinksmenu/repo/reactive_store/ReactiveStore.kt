package com.zemingo.drinksmenu.repo.reactive_store

import kotlinx.coroutines.flow.Flow

interface ReactiveStore<Value> {

    fun getAll(): Flow<List<Value>>

    fun storeAll(data: List<Value>)
}