package com.mbh.moviebrowser.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor() {

    private val favorites = MutableStateFlow(emptyList<Long>())

    fun get(): Flow<List<Long>> = favorites

    fun add(id: Long) {
        favorites.value.toMutableList().also {
            it.add(id)
            favorites.value = it
        }
    }

    fun remove(id: Long) {
        favorites.value.toMutableList().also {
            it.remove(id)
            favorites.value = it
        }
    }
}