package com.apimorlabs.reluct.data.source.database.adapters

import app.cash.sqldelight.EnumColumnAdapter
import com.apimorlabs.reluct.data.source.database.tables.GoalsTable

internal val GoalsTableAdapter = GoalsTable.Adapter(
    relatedAppsAdapter = listOfStringAdapter,
    goalIntervalAdapter = EnumColumnAdapter(),
    timeIntervalAdapter = longRangeAdapter,
    daysOfWeekSelectedAdapter = listOfWeekAdapter,
    goalTypeAdapter = EnumColumnAdapter()
)
