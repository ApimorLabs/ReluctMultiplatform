package work.racka.reluct.data.source.database.adapters

import app.cash.sqldelight.EnumColumnAdapter
import work.racka.reluct.data.source.database.tables.GoalsTable

internal val GoalsTableAdapter = GoalsTable.Adapter(
    relatedAppsAdapter = listOfStringAdapter,
    goalIntervalAdapter = EnumColumnAdapter(),
    timeIntervalAdapter = longRangeAdapter,
    daysOfWeekSelectedAdapter = listOfWeekAdapter,
    goalTypeAdapter = EnumColumnAdapter()
)
