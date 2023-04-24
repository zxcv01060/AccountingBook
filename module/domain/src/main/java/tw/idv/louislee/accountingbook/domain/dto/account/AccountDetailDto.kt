package tw.idv.louislee.accountingbook.domain.dto.account

import tw.idv.louislee.accountingbook.domain.entity.AccountType
import java.time.ZonedDateTime

data class AccountDetailDto(
    val id: Long,
    val type: AccountType,
    val name: String,
    val balance: Long,
    val accountingEvents: List<AccountAccountingEventItemDto>,
    val createDate: ZonedDateTime,
    val lastUpdateDate: ZonedDateTime
)

data class AccountAccountingEventItemDto(
    val id: Long,
    val price: Long,
    val note: String?,
    val recordDate: ZonedDateTime
)
