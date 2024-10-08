package com.apimorlabs.reluct.common.sources

import androidx.lifecycle.ViewModel

class MyViewModel(
    private val repository: MyRepository
) : ViewModel() {

    fun getHelloWorldString(): String = repository.helloWorld()
}
