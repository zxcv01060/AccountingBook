package tw.idv.louislee.accountingbook.domain.dto.accountingevent

import tw.idv.louislee.accountingbook.domain.entity.AccountingEventType

data class AccountingEventDto(
    val id: Long,
    val price: Long,
    val type: AccountingEventType,
    val note: String? = null
)
