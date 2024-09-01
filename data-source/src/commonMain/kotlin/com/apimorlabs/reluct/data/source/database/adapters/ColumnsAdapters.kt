package com.apimorlabs.reluct.data.source.database.adapters

import app.cash.sqldelight.ColumnAdapter
import com.apimorlabs.reluct.common.models.util.time.Week

internal val listOfStringAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String): List<String> =
        if (databaseValue.isBlank()) {
            listOf()
        } else {
            databaseValue.split(";")
        }

    override fun encode(value: List<String>): String = value.joinToString(";")
}

internal val longRangeAdapter = object : ColumnAdapter<LongRange, String> {
    override fun encode(value: LongRange): String = "${value.first}-${value.last}"
    override fun decode(databaseValue: String): LongRange =
        databaseValue.split("-", limit = 2).let { split ->
            LongRange(split.first().toLong(), split.last().toLong())
        }
}

internal val listOfWeekAdapter = object : ColumnAdapter<List<Week>, String> {
    private val enumValues = Week.entries
    override fun encode(value: List<Week>): String = value.joinToString(":")
    override fun decode(databaseValue: String): List<Week> = if (databaseValue.isBlank()) {
        listOf()
    } else {
        databaseValue.split(":")
            .map { enumName -> enumValues.first { it.name == enumName } }
    }
}
