package com.apimorlabs.reluct.domain.pagination

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> Result<Collection<Item>>,
    private val getNextKey: suspend (currentItems: Collection<Item>) -> Key,
    private val onError: suspend (e: Throwable?) -> Unit,
    private val onSuccess: suspend (items: Collection<Item>) -> Unit,
) : Paginator<Key, Item> {

    var currentKey = initialKey
    var isMakingRequest = false

    override suspend fun loadNextItems() {
        if (isMakingRequest) return
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        val items = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }
        currentKey = getNextKey(items)
        onSuccess(items)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialKey
    }
}
