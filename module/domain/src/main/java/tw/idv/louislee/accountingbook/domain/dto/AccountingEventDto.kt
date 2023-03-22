package tw.idv.louislee.accountingbook.domain.dto

import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType
import java.time.ZonedDateTime

data class AccountingEventDto(
    val id: Long,
    val price: Long,
    val isIncome: Boolean,
    val type: AccountingEventType,
    val note: String?,
    val createDate: ZonedDateTime,
    val lastUpdateDate: ZonedDateTime
)
