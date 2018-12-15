package com.nimble.surveys.repository

import androidx.room.TypeConverter
import timber.log.Timber
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun stringToIntList(data: String?): List<Int>? {
        return data?.let {
            it.split(",").map {
                try {
                    it.toInt()
                } catch (ex: NumberFormatException) {
                    Timber.e(ex, "Cannot convert $it to number")
                    null
                }
            }
        }?.filterNotNull()
    }

    @TypeConverter
    fun intListToString(ints: List<Int>?): String? {
        return ints?.joinToString(",")
    }

    @TypeConverter
    fun stringToStringList(data: String?): List<String>? {
        return data?.let { it.split(",") }
    }

    @TypeConverter
    fun stringListToString(ints: List<String>?): String? {
        return ints?.joinToString(",")
    }

    @TypeConverter
    fun calendarToDateStamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun dateStampToCalendar(value: Long): Calendar = Calendar.getInstance().apply { timeInMillis = value }
}