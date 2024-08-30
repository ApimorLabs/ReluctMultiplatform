package com.apimorlabs.reluct.data.source.database.adapters

import app.cash.sqldelight.EnumColumnAdapter
import work.racka.reluct.data.source.database.tables.GoalsTable

internal val GoalsTableAdapter = GoalsTable.Adapter(
    relatedAppsAdapter = com.apimorlabs.reluct.data.source.database.adapters.listOfStringAdapter,
    goalIntervalAdapter = EnumColumnAdapter(),
    timeIntervalAdapter = com.apimorlabs.reluct.data.source.database.adapters.longRangeAdapter,
    daysOfWeekSelectedAdapter = com.apimorlabs.reluct.data.source.database.adapters.listOfWeekAdapter,
    goalTypeAdapter = EnumColumnAdapter()
)
