package com.apimorlabs.reluct.data.source.database.adapters

import work.racka.reluct.data.source.database.tables.TasksTable

internal val TasksTableAdapter = TasksTable.Adapter(
    taskLabelsIdAdapter = com.apimorlabs.reluct.data.source.database.adapters.listOfStringAdapter
)
