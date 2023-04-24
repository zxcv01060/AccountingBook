package tw.idv.louislee.accountingbook.domain.entity.adapter

import com.squareup.sqldelight.ColumnAdapter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

internal class InvoiceDateAdapter : ColumnAdapter<LocalDate, Long> {
    override fun decode(databaseValue: Long): LocalDate =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(databaseValue), ZoneId.of("UTC"))
            .toLocalDate()

    override fun encode(value: LocalDate): Long = LocalDateTime.of(value, LocalTime.MIN)
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli()
}