package com.apimorlabs.reluct.data.source.database.adapters

import com.apimorlabs.reluct.data.source.database.tables.TasksTable

internal val TasksTableAdapter = TasksTable.Adapter(
    taskLabelsIdAdapter = listOfStringAdapter
)
