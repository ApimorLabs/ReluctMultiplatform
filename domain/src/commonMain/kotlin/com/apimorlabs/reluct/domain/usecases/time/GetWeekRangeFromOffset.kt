package com.apimorlabs.reluct.domain.usecases.time

interface GetWeekRangeFromOffset {
    suspend fun getOffset(weekOffset: Int): String
}
