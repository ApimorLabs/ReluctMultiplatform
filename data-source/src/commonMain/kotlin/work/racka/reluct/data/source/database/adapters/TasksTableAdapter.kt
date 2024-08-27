package work.racka.reluct.data.source.database.adapters

import work.racka.reluct.data.source.database.tables.TasksTable

internal val TasksTableAdapter = TasksTable.Adapter(
    taskLabelsIdAdapter = listOfStringAdapter
)
