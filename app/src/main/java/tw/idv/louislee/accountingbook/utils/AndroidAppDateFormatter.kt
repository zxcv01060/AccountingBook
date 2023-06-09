package tw.idv.louislee.accountingbook.utils

import android.content.Context
import org.koin.core.annotation.Factory
import tw.idv.louislee.accountingbook.R
import tw.idv.louislee.accountingbook.domain.utils.AppDateFormatter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Factory
internal class AndroidAppDateFormatter(context: Context) : AppDateFormatter {
    private val dateFormatter by lazy {
        DateTimeFormatter.ofPattern(context.getString(R.string.common_date_format))
    }
    private val timeFormatter by lazy {
        DateTimeFormatter.ofPattern(context.getString(R.string.common_time_format))
    }
    private val timeIncludeSecondFormatter by lazy {
        DateTimeFormatter.ofPattern(context.getString(R.string.common_time_include_second_format))
    }
    private val dateTimeFormatter by lazy {
        DateTimeFormatter.ofPattern(context.getString(R.string.common_date_time_format))
    }
    private val dateTimeIncludeSecondFormatter by lazy {
        DateTimeFormatter.ofPattern(context.getString(R.string.common_date_time_include_second_format))
    }

    override fun formatDate(date: ZonedDateTime): String = date.format(dateFormatter)
    override fun formatDate(date: LocalDateTime): String = date.format(dateFormatter)

    override fun formatDate(date: LocalDate): String = date.format(dateFormatter)

    override fun formatTime(time: ZonedDateTime): String = time.format(timeFormatter)
    override fun formatTime(time: LocalDateTime): String = time.format(timeFormatter)

    override fun formatTime(time: LocalTime): String = time.format(timeFormatter)

    override fun formatTimeIncludeSecond(time: ZonedDateTime): String =
        time.format(timeIncludeSecondFormatter)

    override fun formatTimeIncludeSecond(time: LocalDateTime): String =
        time.format(timeIncludeSecondFormatter)

    override fun formatTimeIncludeSecond(time: LocalTime): String =
        time.format(timeIncludeSecondFormatter)

    override fun formatDateTime(dateTime: ZonedDateTime): String =
        dateTime.format(dateTimeFormatter)

    override fun formatDateTime(dateTime: LocalDateTime): String =
        dateTime.format(dateTimeIncludeSecondFormatter)

    override fun formatDateTimeIncludeSecond(dateTime: ZonedDateTime): String =
        dateTime.format(dateTimeIncludeSecondFormatter)

    override fun formatDateTimeIncludeSecond(dateTime: LocalDateTime): String =
        dateTime.format(dateTimeIncludeSecondFormatter)
}