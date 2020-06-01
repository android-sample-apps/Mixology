package com.yanivsos.mixological.repo.reactiveStore

import kotlinx.coroutines.flow.Flow

interface NonRemovableReactiveStore<Value, Param> {
    fun get(param: Param): Flow<List<Value>>
    fun storeAll(data: List<Value>)
}

interface RemovableReactiveStore<Key, Value, Param> : NonRemovableReactiveStore<Value, Param> {
    fun remove(keys: List<Key>)
}

interface RemoveAllReactiveStore<Key, Value, Param> : RemovableReactiveStore<Key, Value, Param> {
    fun removeAll()
}


