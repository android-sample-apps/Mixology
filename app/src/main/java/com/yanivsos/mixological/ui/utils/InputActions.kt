package com.yanivsos.mixological.ui.utils

sealed class InputActions<T> {
    data class Click<T>(val data: T) : InputActions<T>()
    data class LongClick<T>(val data: T) : InputActions<T>()
}