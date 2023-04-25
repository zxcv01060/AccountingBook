package tw.idv.louislee.accountingbook.domain.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime

interface AppDateFormatter {
    fun formatDate(date: ZonedDateTime): String

    fun formatDate(date: LocalDateTime): String

    fun formatDate(date: LocalDate): String

    fun formatTime(time: ZonedDateTime): String

    fun formatTime(time: LocalDateTime): String

    fun formatTime(time: LocalTime): String

    fun formatTimeIncludeSecond(time: ZonedDateTime): String

    fun formatTimeIncludeSecond(time: LocalDateTime): String

    fun formatTimeIncludeSecond(time: LocalTime): String

    fun formatDateTime(dateTime: ZonedDateTime): String

    fun formatDateTimeIncludeSecond(dateTime: ZonedDateTime): String

    fun formatDateTime(dateTime: LocalDateTime): String

    fun formatDateTimeIncludeSecond(dateTime: LocalDateTime): String
}
