package com.apimorlabs.reluct.data.source.database.dao.goals

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import com.apimorlabs.reluct.data.source.di.getDatabaseWrapper
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class GoalsDaoImplTest {

    private lateinit var dao: GoalsDao

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        dao = GoalsDaoImpl(
            dispatcher = StandardTestDispatcher(),
            databaseWrapper = getDatabaseWrapper()
        )
    }

    @AfterTest
    fun teardown() {
    }
}