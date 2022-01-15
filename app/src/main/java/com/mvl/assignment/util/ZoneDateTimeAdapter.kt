package com.mvl.assignment.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZoneDateTimeAdapter {
    @FromJson
    fun fromJson(json: String?): ZonedDateTime? {
        if (json.isNullOrEmpty()) {
            return null
        }
        val localDate = LocalDate.parse(json, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return localDate.atStartOfDay(ZoneId.systemDefault())
    }

    @ToJson
    fun toJson(value:ZonedDateTime?):String?{
        return value?.toString()
    }
}