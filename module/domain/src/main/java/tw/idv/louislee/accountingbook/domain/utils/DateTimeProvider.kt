package tw.idv.louislee.accountingbook.domain.utils

import org.koin.core.annotation.Single
import java.time.Clock
import java.time.ZonedDateTime

interface DateTimeProvider {
    val now: ZonedDateTime
}

@Single
internal class UtcDateTimeProvider : DateTimeProvider {
    override val now: ZonedDateTime
        get() = ZonedDateTime.now(Clock.systemUTC())

}
