package tw.idv.louislee.accountingbook.domain.entity.adapter

import com.squareup.sqldelight.ColumnAdapter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

internal class DateTimeAdapter : ColumnAdapter<ZonedDateTime, Long> {
    override fun decode(databaseValue: Long): ZonedDateTime {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(databaseValue), ZoneId.systemDefault())
    }

    override fun encode(value: ZonedDateTime): Long = value.epochMillisecond

    private val ZonedDateTime.epochMillisecond: Long
        get() {
            val instant = toInstant()

            return instant.toEpochMilli()
        }
}