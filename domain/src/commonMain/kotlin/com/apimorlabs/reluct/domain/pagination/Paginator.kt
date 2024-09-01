package com.apimorlabs.reluct.domain.pagination

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}
